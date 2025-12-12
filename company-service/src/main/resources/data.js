use
db;

db.createCollection("company");


db.company.insertOne(
    {
        _id: "8d3457d3-1841-4750-9b3b-9351a789ab26",
        name: "Ferreterías Unidos S.A.",
        nit: "900123456",
        owner_user_id: "180f1d0a-522d-4194-ac5e-51909104975b",
        plan: "premium",
        settings: {
            currency: "COP",
            language: "es"
        },
        created_at: "2025-01-01"
    }
)


