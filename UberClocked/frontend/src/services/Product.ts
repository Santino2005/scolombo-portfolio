import type { Product } from "../types/Entities.ts";
import { API_BASE_URL } from "@/config/api";

const BASE = API_BASE_URL;

export async function getProducts(): Promise<Product[]> {
    try {
        const r = await fetch(`${BASE}/products`);
        if (!r.ok) return [];
        const data = await r.json();
        return Array.isArray(data) ? data : [];
    } catch (e) {
        console.warn("Could not connect to API server at", BASE);
        return [];
    }
}

export async function getFilteredProductsPublic(
    params: Record<string, string>
): Promise<Product[]> {
    try {
        const query = new URLSearchParams(params).toString();
        const r = await fetch(`${BASE}/products/filter?${query}`);
        if (!r.ok) return [];
        const data = await r.json();
        return Array.isArray(data) ? data : [];
    } catch (e) {
        console.warn("Could not connect to API server at", BASE);
        return [];
    }
}

export async function getProductBySkuPublic(skuPrefix: string): Promise<Product | null> {
    try {
        const r = await fetch(`${BASE}/products/${encodeURIComponent(skuPrefix)}`);
        if (!r.ok) return null;
        return await r.json();
    } catch {
        return null;
    }
}

export async function getProductsByComponentPrefix(componentSkuPrefix: string): Promise<Product[]> {
    try {
        const url = `${BASE}/products/filter?componentSkuPrefix=${encodeURIComponent(componentSkuPrefix)}`;
        const res = await fetch(url);
        if (!res.ok) return [];
        const data = await res.json();
        return Array.isArray(data) ? data : [];
    } catch {
        return [];
    }
}
