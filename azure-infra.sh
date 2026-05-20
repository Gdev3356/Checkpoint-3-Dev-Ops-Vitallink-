#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# VitalLink — Provisionamento de infraestrutura no Azure
# Cria: Resource Group, VNet, Subnet, NSG, VM Standard_B4ls_v2 (8 GB RAM),
#       abre portas 22, 8080 e 1521, instala Docker e ferramentas na VM.
#
# Uso:
#   chmod +x azure-infra.sh
#   az login
#   ./azure-infra.sh
# ─────────────────────────────────────────────────────────────────────────────
set -e

#Variáveis
RG="rg-vitallink-challenge"
LOCATION="southafricanorth"
VNET="vnet-vitallink-dev"
SUBNET="subnet-vitallink-app"
NSG="nsg-vitallink-web"
VM="vm-vitallink-dev-01"
ADMIN="azureuser"

echo "VitalLink — Provisionamento Azure CLI"


#1. Resource Group
echo "[1/6] Criando Resource Group '$RG' em '$LOCATION'..."
az group create \
  --name "$RG" \
  --location "$LOCATION"

#2. VNet + Subnet
echo "[2/6] Criando VNet e Subnet..."
az network vnet create \
  --resource-group "$RG" \
  --location "$LOCATION" \
  --name "$VNET" \
  --address-prefixes 10.10.0.0/16 \
  --subnet-name "$SUBNET" \
  --subnet-prefixes 10.10.1.0/24

#3. NSG
echo "[3/6] Criando Network Security Group..."
az network nsg create \
  --resource-group "$RG" \
  --location "$LOCATION" \
  --name "$NSG"

#4. VM Standard_B4ls_v2 (8 GB RAM — necessário para Oracle XE)
echo "[4/6] Criando VM '$VM' (Standard_B4ls_v2 — Ubuntu 22.04)..."
az vm create \
  --resource-group "$RG" \
  --name "$VM" \
  --image Ubuntu2204 \
  --size Standard_B4ls_v2 \
  --admin-username "$ADMIN" \
  --generate-ssh-keys \
  --vnet-name "$VNET" \
  --subnet "$SUBNET" \
  --nsg "$NSG" \
  --output json

#5. Abrir portas
echo "[5/6] Abrindo portas: 22 (SSH), 8080 (App), 1521 (Oracle)..."
az vm open-port --resource-group "$RG" --name "$VM" --port 22   --priority 100
az vm open-port --resource-group "$RG" --name "$VM" --port 8080 --priority 110
az vm open-port --resource-group "$RG" --name "$VM" --port 1521 --priority 120

#Instalar Docker e ferramentas via run-command
echo "[6/6] Instalando Docker, Git, Nano e Curl na VM..."
az vm run-command invoke \
  --resource-group "$RG" \
  --name "$VM" \
  --command-id RunShellScript \
  --scripts "
    sudo apt-get update -y
    sudo apt-get install -y git nano curl ca-certificates
    curl -fsSL https://get.docker.com | sudo sh
    sudo usermod -aG docker $ADMIN
    echo 'Instalação concluída.'
  "

#IP público
echo "  Infraestrutura provisionada com sucesso!"
echo ""
echo "IP público da VM:"
az vm show \
  --resource-group "$RG" \
  --name "$VM" \
  --show-details \
  --query publicIps \
  --output tsv

echo ""
echo "Próximos passos:"
echo "  ssh $ADMIN@<IP_PUBLICO>"
echo "  docker pull rm563428/vitallink:v1"
echo "  docker pull gvenzl/oracle-xe:21-slim"
echo "  git clone https://github.com/Gdev3356/Checkpoint-3-Dev-Ops-Vitallink-"
echo "  cd Checkpoint-3-Dev-Ops-Vitallink-/"
echo "  sudo docker compose up -d"
