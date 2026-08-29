import { useEffect, useState } from "react";
import { ChevronLeft, ChevronRight, Sparkles } from "lucide-react";
import { ProductCard } from "../../../ProductCard";
import type { Props } from "./ProductCarousel.types";
import { getProductRating, getReviewsByProduct } from "@/services/Review";
import type { ProductRatingDto, ReviewResponseDto } from "@/types/Review";

export default function ProductCarousel({ products }: Props) {
    const [index, setIndex] = useState(0);
    const [rating, setRating] = useState<ProductRatingDto | null>(null);
    const [firstReview, setFirstReview] = useState<ReviewResponseDto | null>(null);
    const [isPaused, setIsPaused] = useState(false);

    const activeProducts = products.filter((p) => p && p.active && p.stock > 0);

    // Auto rotate every 4.5 seconds unless user hovers
    useEffect(() => {
        if (activeProducts.length <= 1 || isPaused) return;

        const interval = setInterval(() => {
            setIndex((prev) => (prev + 1) % activeProducts.length);
        }, 4500);

        return () => clearInterval(interval);
    }, [activeProducts.length, isPaused]);

    // Load review and rating for current featured product
    useEffect(() => {
        if (activeProducts.length === 0) return;

        const p = activeProducts[index];
        if (!p?.skuPrefix) return;

        let cancelled = false;

        (async () => {
            try {
                const [rt, rv] = await Promise.all([
                    getProductRating(p.skuPrefix),
                    getReviewsByProduct(p.skuPrefix),
                ]);

                if (cancelled) return;
                setRating(rt ?? null);
                setFirstReview((rv?.[0] ?? null) as any);
            } catch {
                if (cancelled) return;
                setRating(null);
                setFirstReview(null);
            }
        })();

        return () => {
            cancelled = true;
        };
    }, [index, activeProducts]);

    if (activeProducts.length === 0) {
        return (
            <div className="w-full py-12 text-center rounded-2xl border border-zinc-800 bg-zinc-950/60 text-zinc-400">
                <p>No featured products available at the moment.</p>
            </div>
        );
    }

    const currentProduct = activeProducts[index];

    const prevSlide = () => {
        setIndex((prev) => (prev === 0 ? activeProducts.length - 1 : prev - 1));
    };

    const nextSlide = () => {
        setIndex((prev) => (prev + 1) % activeProducts.length);
    };

    return (
        <div
            className="relative w-full max-w-5xl mx-auto space-y-4"
            onMouseEnter={() => setIsPaused(true)}
            onMouseLeave={() => setIsPaused(false)}
        >
            <div className="flex items-center justify-between px-1">
                <div className="flex items-center gap-2 text-xs font-black uppercase tracking-widest text-orange-400">
                    <Sparkles className="w-4 h-4" /> Featured Flagship Deals
                </div>
                <div className="flex items-center gap-1.5">
                    <button
                        type="button"
                        onClick={prevSlide}
                        className="w-8 h-8 rounded-xl bg-zinc-900 border border-zinc-800 hover:border-orange-500/50 hover:bg-zinc-800 text-zinc-300 hover:text-white flex items-center justify-center transition shadow"
                        aria-label="Previous product"
                    >
                        <ChevronLeft className="w-4 h-4" />
                    </button>
                    <button
                        type="button"
                        onClick={nextSlide}
                        className="w-8 h-8 rounded-xl bg-zinc-900 border border-zinc-800 hover:border-orange-500/50 hover:bg-zinc-800 text-zinc-300 hover:text-white flex items-center justify-center transition shadow"
                        aria-label="Next product"
                    >
                        <ChevronRight className="w-4 h-4" />
                    </button>
                </div>
            </div>

            <div className="relative">
                <ProductCard
                    product={currentProduct}
                    rating={rating}
                    firstReview={firstReview}
                    variant="featured"
                />
            </div>

            {/* Pagination Dots */}
            {activeProducts.length > 1 && (
                <div className="flex items-center justify-center gap-1.5 pt-2">
                    {activeProducts.slice(0, 10).map((_, i) => (
                        <button
                            key={i}
                            type="button"
                            onClick={() => setIndex(i)}
                            className={`h-1.5 rounded-full transition-all duration-300 ${
                                i === index ? "w-6 bg-orange-500 shadow-sm shadow-orange-500/50" : "w-1.5 bg-zinc-700 hover:bg-zinc-500"
                            }`}
                            aria-label={`Go to slide ${i + 1}`}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}