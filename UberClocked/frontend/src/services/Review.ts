import type { ReviewResponseDto, CreateReviewDto, ModifyReviewDataDto, ProductRatingDto } from "../types/Review.ts";
import { fetchWithAuth } from "./api";
import { API_BASE_URL } from "@/config/api";

const BASE = API_BASE_URL;

export async function getReviewsByProduct(skuPrefix: string): Promise<ReviewResponseDto[]> {
    try {
        const res = await fetch(`${BASE}/reviews/product/${encodeURIComponent(skuPrefix)}`);
        if (!res.ok) return [];
        const data = await res.json();
        return Array.isArray(data) ? data : [];
    } catch {
        return [];
    }
}

export async function getProductRating(skuPrefix: string): Promise<ProductRatingDto | null> {
    try {
        const res = await fetch(`${BASE}/reviews/product/${encodeURIComponent(skuPrefix)}/rating`);
        if (!res.ok) return null;
        return await res.json();
    } catch {
        return null;
    }
}

export async function createReview(token: string, dto: CreateReviewDto): Promise<ReviewResponseDto> {
    return fetchWithAuth(`${BASE}/reviews`, token, {
        method: "POST",
        body: JSON.stringify(dto),
    });
}

export async function updateReview(token: string, id: string, dto: ModifyReviewDataDto): Promise<ReviewResponseDto> {
    return fetchWithAuth(`${BASE}/reviews/${id}`, token, {
        method: "PATCH",
        body: JSON.stringify(dto),
    });
}

export async function deleteReview(token: string, id: string) {
    return fetchWithAuth(`${BASE}/reviews/${id}`, token, { method: "DELETE" });
}

export async function getMyReviews(token: string): Promise<ReviewResponseDto[]> {
    return fetchWithAuth(`${BASE}/reviews/me`, token, { method: "GET" });
}

export async function getAllReviews(token: string, skuPrefix?: string): Promise<ReviewResponseDto[]> {
    const qs = skuPrefix && skuPrefix.trim() ? `?skuPrefix=${encodeURIComponent(skuPrefix.trim())}` : "";
    return fetchWithAuth(`${BASE}/reviews${qs}`, token, { method: "GET" });
}
