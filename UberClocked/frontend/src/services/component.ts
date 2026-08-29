import type { Component } from "@/pages/builder/types/Component";
import { fetchWithAuth } from "@/services/api.ts";
import { API_BASE_URL } from "@/config/api";

const BASE = API_BASE_URL;

export type ComponentDto = { skuPrefix: string; displayName: string };

export async function getAll(): Promise<Component[]> {
    try {
        const res = await fetch(`${BASE}/components`);
        if (!res.ok) return [];
        const data = await res.json();
        return Array.isArray(data)
            ? data.map((c: any) => ({
                  id: c.skuPrefix,
                  sku_prefix: c.skuPrefix,
                  display_name: c.displayName,
              }))
            : [];
    } catch {
        return [];
    }
}

export async function getComponents(token: string) {
    return fetchWithAuth<ComponentDto[]>(`${BASE}/components`, token);
}

export async function getComponentsPublic(): Promise<ComponentDto[]> {
    try {
        const res = await fetch(`${BASE}/components`);
        if (!res.ok) return [];
        const data = await res.json();
        return Array.isArray(data) ? data : [];
    } catch {
        return [];
    }
}
