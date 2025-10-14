# 📋 Exemples de JSON pour tester l'API Product

## ✅ Exemple valide complet

```json
{
  "name": "Smartphone Samsung Galaxy S24",
  "description": "Smartphone haut de gamme avec écran AMOLED 6.8 pouces, 256GB de stockage, caméra 108MP et charge rapide 45W",
  "price": 899.99,
  "quantityInStock": 25,
  "category": "Electronics",
  "sku": "SAM-S24-256GB",
  "active": true
}
```

## ✅ Exemple minimal (champs optionnels omis)

```json
{
  "name": "Laptop Dell XPS 13",
  "price": 1299.99,
  "quantityInStock": 10
}
```

## ✅ Autres exemples valides

### Produit alimentaire
```json
{
  "name": "Café Bio Équitable",
  "description": "Café arabica 100% bio et équitable, torréfaction française",
  "price": 15.90,
  "quantityInStock": 50,
  "category": "Food & Beverages",
  "sku": "CAFE-BIO-250G",
  "active": true
}
```

### Livre
```json
{
  "name": "Clean Code - Robert C. Martin",
  "description": "Guide pratique pour écrire du code propre et maintenable",
  "price": 45.00,
  "quantityInStock": 15,
  "category": "Books",
  "sku": "BOOK-CLEAN-CODE",
  "active": true
}
```

### Vêtement
```json
{
  "name": "T-Shirt Coton Bio - Taille M",
  "description": "T-shirt en coton biologique, coupe moderne, disponible en plusieurs couleurs",
  "price": 29.99,
  "quantityInStock": 100,
  "category": "Clothing",
  "sku": "TSHIRT-BIO-M-BLUE",
  "active": true
}
```

## ❌ Exemples qui génèrent des erreurs de validation

### Nom trop court
```json
{
  "name": "A",
  "price": 10.00,
  "quantityInStock": 5
}
```
**Erreur :** Le nom doit contenir entre 2 et 100 caractères

### Prix négatif
```json
{
  "name": "Produit Test",
  "price": -5.00,
  "quantityInStock": 10
}
```
**Erreur :** Le prix doit être supérieur à 0

### Champs obligatoires manquants
```json
{
  "name": "Produit Test",
  "description": "Description"
}
```
**Erreur :** Le prix ne peut pas être null, La quantité en stock ne peut pas être null

### SKU trop long
```json
{
  "name": "Produit Test",
  "price": 25.00,
  "quantityInStock": 10,
  "sku": "SKU-VRAIMENT-TRES-LONG-PLUS-DE-20-CARACTERES"
}
```
**Erreur :** Le SKU ne peut pas dépasser 20 caractères

## 🧪 Commandes curl pour tester

### Test avec un produit valide
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone Samsung Galaxy S24",
    "description": "Smartphone haut de gamme avec écran AMOLED 6.8 pouces",
    "price": 899.99,
    "quantityInStock": 25,
    "category": "Electronics",
    "sku": "SAM-S24-256GB",
    "active": true
  }'
```

### Test avec données invalides (pour tester la validation)
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "A",
    "price": -10.00,
    "quantityInStock": 5
  }'
```

## 📊 Réponses attendues

### ✅ Succès (201 Created)
```json
{
  "id": 1,
  "name": "Smartphone Samsung Galaxy S24",
  "description": "Smartphone haut de gamme avec écran AMOLED 6.8 pouces",
  "price": 899.99,
  "quantityInStock": 25,
  "category": "Electronics",
  "sku": "SAM-S24-256GB",
  "active": true,
  "createdAt": "2025-10-14T10:30:45.123456",
  "updatedAt": "2025-10-14T10:30:45.123456",
  "available": true
}
```

### ❌ Erreur de validation (400 Bad Request)
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Les données fournies ne sont pas valides",
  "timestamp": "2025-10-14T10:30:45.123456",
  "path": "/api/v1/products",
  "fieldErrors": [
    {
      "field": "name",
      "rejectedValue": "A",
      "message": "Le nom doit contenir entre 2 et 100 caractères"
    },
    {
      "field": "price",
      "rejectedValue": -10.0,
      "message": "Le prix doit être supérieur à 0"
    }
  ]
}
```