# ⚡ UberClocked – Next-Gen PC Building & Hardware E-Commerce

> **Explore the live platform:** 🚀 [**https://uberclocked.vercel.app**](https://uberclocked.vercel.app)

---

## 🎯 What is UberClocked?

**UberClocked** is an end-to-end e-commerce and interactive PC building platform crafted specifically for gamers, power users, and hardware enthusiasts. 

Instead of dealing with confusing spreadsheets or risking incompatible parts, UberClocked provides a sleek, modern, and guided experience to configure, customize, and purchase dream PC setups. From flagship RTX 4090 builds and Ryzen 7 7800X3D gaming rigs to everyday workstations, UberClocked makes custom computer building transparent, reliable, and exciting.

---

## ✨ Key Features & User Experience

### 🖥️ Interactive PC Builder & Real-Time Compatibility
- Configure complete custom rigs step-by-step: **CPU, GPU, Motherboard, RAM, Storage (SSD/HDD), Case, PSU, Cooler, Monitor, and Peripherals**.
- Real-time compatibility checks across sockets (AM5, LGA1700, LGA1851), memory generations (DDR4 / DDR5), form factors, and power budgets.
- Add individual components or complete assembled builds directly to the cart.

### 📦 Dynamic Hardware Catalog (200+ Products)
- Rich technical specifications for all hardware components.
- Instant multi-attribute search and filtering (by brand, socket, chipset, VRAM, capacity, wattage, and price range).
- High-definition product visuals and quick-spec modal inspections.

### 💳 Seamless Checkout with Mercado Pago
- Transparent pricing and real-time total calculations.
- Integrated **Mercado Pago** gateway for fast and secure checkout.
- Automated email order confirmation and purchase history tracking.

### 🎡 Gamification & Loyalty (Lucky Discount Wheel)
- Daily interactive discount wheel for users to spin and unlock exclusive coupon codes (e.g., `OVERCLOCK10`, `SANTINO20`).
- Seamless coupon application during checkout.

### 🤝 Community Marketplace
- Dedicated peer-to-peer marketplace to buy and sell pre-owned gaming gear and components.
- Transparent seller profiles and listing status tracking.

### ⭐ Verified Customer Reviews & Benchmarks
- Star rating system and written reviews for individual hardware products.
- Benchmark verification tags to help buyers make informed decisions.

### 🎨 Adaptive Theme & Dynamic Accent Engine
- Fully customizable accent colors: **Über Flame (Orange), Electric Ice (Cyan), Matrix Pro (Emerald), Hyper RGB (Violet), Crimson ROG (Rose), and Overclock Gold (Amber)**.
- Dark mode by default with light mode toggle.

### 🛡️ Administrative Control Center
- Role-based access control powered by Auth0.
- Admin dashboard to manage dynamic component schemas, upload product images, monitor orders, review marketplace posts, and configure discount promotions.

---

## 🏗️ Technical Architecture

```
                                 ┌────────────────────────┐
                                 │   Auth0 OAuth2 / JWT   │
                                 └───────────┬────────────┘
                                             │
┌────────────────────────────┐               │               ┌───────────────────────────┐
│     React 19 Frontend      │◄──────────────┴──────────────►│    Spring Boot Backend    │
│ (Vite, TS, Tailwind, Radix)│       REST API (JSON)         │    (Java 21, JPA, Sec)    │
└────────────────────────────┘                               └─────────────┬─────────────┘
              │                                                            │
              │                                              ┌─────────────┴─────────────┐
              ▼                                              │    PostgreSQL (Supabase)  │
┌────────────────────────────┐                               │    HikariCP & Hibernate   │
│   Mercado Pago SDK         │                               └───────────────────────────┘
└────────────────────────────┘
```

- **Frontend:** React 19, TypeScript, Vite, Tailwind CSS, Radix UI, Lucide Icons, Auth0 React SDK.
- **Backend:** Java 21, Spring Boot, Spring Security (OAuth2 Resource Server with JWT validation), Spring Data JPA, Hibernate ORM.
- **Database & Cloud:** PostgreSQL on Supabase, HikariCP connection pooling, Vercel frontend deployment, Render backend deployment.
- **Payment & Mail:** Mercado Pago REST API integration, JavaMailSender with Gmail SMTP.

---

## 🌐 Try It Out

Experience the full platform live at:  
👉 **[https://uberclocked.vercel.app](https://uberclocked.vercel.app)**
