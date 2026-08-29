import { useAuth0 } from "@auth0/auth0-react";
import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "react-router-dom";
import {
    Search,
    Filter,
    SlidersHorizontal,
    X,
    Sparkles,
    Check,
    Cpu,
    Flame,
    Layers,
    MemoryStick,
    HardDrive,
    Zap,
    Fan,
    Monitor,
    MousePointer,
    Tag
} from "lucide-react";
import ProductCard from "@/components/ProductCard";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { getFilteredProductsPublic } from "@/services/Product";
import type { Product } from "@/types/Entities";

const PAGE_SIZE = 12;

type ComponentDto = { skuPrefix: string; displayName: string };

const FALLBACK_COMPONENTS: ComponentDto[] = [
    { skuPrefix: "CPU", displayName: "Processors" },
    { skuPrefix: "GPU", displayName: "Graphics Cards" },
    { skuPrefix: "MOTHERBOARD", displayName: "Motherboards" },
    { skuPrefix: "RAM", displayName: "RAM Memory" },
    { skuPrefix: "SD", displayName: "Storage SSD/HDD" },
    { skuPrefix: "CASE", displayName: "Cases & Chassis" },
    { skuPrefix: "PSU", displayName: "Power Supplies" },
    { skuPrefix: "COOLER", displayName: "CPU Coolers" },
    { skuPrefix: "MONITOR", displayName: "Monitors" },
    { skuPrefix: "PERIPHERAL", displayName: "Peripherals" },
];

export default function ProductsUser() {
    const [searchParams, setSearchParams] = useSearchParams();
    const initialComponent = searchParams.get("component") || "ALL";

    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [searchQuery, setSearchQuery] = useState("");
    const [attributeFilter, setAttributeFilter] = useState("");
    const [components, setComponents] = useState<ComponentDto[]>(FALLBACK_COMPONENTS);
    const [mobileFiltersOpen, setMobileFiltersOpen] = useState(false);

    const [filters, setFilters] = useState<Record<string, string>>({
        componentSkuPrefix: initialComponent,
        minPrice: "",
        maxPrice: "",
    });

    const [sortBy, setSortBy] = useState<"featured" | "price_asc" | "price_desc" | "name">("featured");

    // Fetch component list publicly
    useEffect(() => {
        let mounted = true;
        (async () => {
            try {
                const res = await fetch(`${(import.meta.env.VITE_API_URL as string) || "http://localhost:8080"}/components`);
                if (res.ok) {
                    const data = await res.json();
                    if (mounted && Array.isArray(data) && data.length > 0) {
                        setComponents(data);
                    }
                }
            } catch {
                // Keep fallback
            }
        })();
        return () => {
            mounted = false;
        };
    }, []);

    // Fetch products
    useEffect(() => {
        let mounted = true;
        setLoading(true);
        (async () => {
            try {
                const data = await getFilteredProductsPublic(filters);
                if (mounted) {
                    setProducts(data.filter((p: any) => p && p.active && (p.stock ?? 0) > 0));
                }
            } catch (err) {
                console.error(err);
                if (mounted) setProducts([]);
            } finally {
                if (mounted) setLoading(false);
            }
        })();
        return () => {
            mounted = false;
        };
    }, [filters]);

    // Apply local search and sorting
    const processedProducts = useMemo(() => {
        let result = [...products];

        // Search query
        if (searchQuery.trim()) {
            const q = searchQuery.toLowerCase();
            result = result.filter(
                (p) =>
                    p.name.toLowerCase().includes(q) ||
                    p.skuPrefix.toLowerCase().includes(q) ||
                    Object.values(p.attributes ?? {}).some((v) => String(v).toLowerCase().includes(q))
            );
        }

        // Sorting
        if (sortBy === "price_asc") {
            result.sort((a, b) => Number(a.price) - Number(b.price));
        } else if (sortBy === "price_desc") {
            result.sort((a, b) => Number(b.price) - Number(a.price));
        } else if (sortBy === "name") {
            result.sort((a, b) => a.name.localeCompare(b.name));
        }

        return result;
    }, [products, searchQuery, sortBy]);

    const paginated = useMemo(() => {
        const start = page * PAGE_SIZE;
        return processedProducts.slice(start, start + PAGE_SIZE);
    }, [processedProducts, page]);

    const totalPages = Math.ceil(processedProducts.length / PAGE_SIZE) || 1;

    function handleComponentSelect(skuPrefix: string) {
        setPage(0);
        setFilters((prev) => ({ ...prev, componentSkuPrefix: skuPrefix }));
        setSearchParams(skuPrefix === "ALL" ? {} : { component: skuPrefix });
    }

    function updatePriceFilter(key: "minPrice" | "maxPrice", val: string) {
        const clean = val.replace(/[^\d.]/g, "");
        setPage(0);
        setFilters((prev) => ({ ...prev, [key]: clean }));
    }

    function clearAllFilters() {
        setPage(0);
        setSearchQuery("");
        setAttributeFilter("");
        setSortBy("featured");
        setFilters({ componentSkuPrefix: "ALL", minPrice: "", maxPrice: "" });
        setSearchParams({});
    }

    return (
        <div className="w-full min-h-screen bg-zinc-950 text-white p-4 sm:p-6 lg:p-8">
            <div className="max-w-7xl mx-auto space-y-6">
                {/* Page Title & Stats */}
                <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 border-b border-zinc-800/80 pb-6">
                    <div>
                        <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-orange-500/10 text-orange-400 text-xs font-black uppercase tracking-widest mb-1.5">
                            <Tag className="w-3.5 h-3.5" /> Hardware Marketplace
                        </div>
                        <h1 className="text-3xl sm:text-4xl font-black tracking-tight text-white">
                            Explore Components & Hardware
                        </h1>
                        <p className="text-sm text-zinc-400 mt-1">
                            Browse benchmarked parts, filter by socket or speed, and upgrade your rig today.
                        </p>
                    </div>

                    <div className="flex items-center gap-2 self-start sm:self-auto">
                        <span className="text-xs text-zinc-400">
                            Showing <span className="font-bold text-orange-400">{processedProducts.length}</span> parts
                        </span>
                        <Button
                            variant="outline"
                            size="sm"
                            className="lg:hidden rounded-xl border-zinc-800 bg-zinc-900 text-xs font-bold text-zinc-200"
                            onClick={() => setMobileFiltersOpen((prev) => !prev)}
                        >
                            <SlidersHorizontal className="w-3.5 h-3.5 mr-1.5 text-orange-400" />
                            Filters
                        </Button>
                    </div>
                </div>

                {/* Horizontal Category Filter Pills (Mobile First Scrollable) */}
                <div className="overflow-x-auto pb-2 scrollbar-none flex items-center gap-2">
                    <button
                        type="button"
                        onClick={() => handleComponentSelect("ALL")}
                        className={`px-4 py-2 rounded-xl text-xs font-extrabold whitespace-nowrap transition-all flex items-center gap-1.5 shadow-sm ${
                            filters.componentSkuPrefix === "ALL"
                                ? "bg-orange-500 text-white shadow-orange-500/30"
                                : "bg-zinc-900/90 text-zinc-300 hover:text-white hover:bg-zinc-800 border border-zinc-800/80"
                        }`}
                    >
                        All Categories
                    </button>
                    {components.map((c) => {
                        const isSelected = filters.componentSkuPrefix === c.skuPrefix;
                        return (
                            <button
                                key={c.skuPrefix}
                                type="button"
                                onClick={() => handleComponentSelect(c.skuPrefix)}
                                className={`px-4 py-2 rounded-xl text-xs font-extrabold whitespace-nowrap transition-all flex items-center gap-1.5 shadow-sm ${
                                    isSelected
                                        ? "bg-orange-500 text-white shadow-orange-500/30"
                                        : "bg-zinc-900/90 text-zinc-300 hover:text-white hover:bg-zinc-800 border border-zinc-800/80"
                                }`}
                            >
                                <span>{c.displayName}</span>
                            </button>
                        );
                    })}
                </div>

                {/* Search & Filter Bar */}
                <div className="p-4 rounded-2xl bg-zinc-900/60 border border-zinc-800/80 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3 items-center">
                    {/* Search Input */}
                    <div className="relative lg:col-span-2">
                        <Search className="w-4 h-4 text-zinc-400 absolute left-3.5 top-1/2 -translate-y-1/2 pointer-events-none" />
                        <Input
                            placeholder="Search by part name, SKU, chip, or brand..."
                            value={searchQuery}
                            onChange={(e) => {
                                setPage(0);
                                setSearchQuery(e.target.value);
                            }}
                            className="pl-10 bg-zinc-950/80 border-zinc-800 text-white placeholder:text-zinc-500 rounded-xl text-xs"
                        />
                        {searchQuery && (
                            <button
                                type="button"
                                onClick={() => setSearchQuery("")}
                                className="absolute right-3 top-1/2 -translate-y-1/2 text-zinc-400 hover:text-white"
                            >
                                <X className="w-3.5 h-3.5" />
                            </button>
                        )}
                    </div>

                    {/* Price Range */}
                    <div className="grid grid-cols-2 gap-2">
                        <Input
                            type="text"
                            inputMode="decimal"
                            placeholder="Min $"
                            value={filters.minPrice}
                            onChange={(e) => updatePriceFilter("minPrice", e.target.value)}
                            className="bg-zinc-950/80 border-zinc-800 text-white placeholder:text-zinc-500 rounded-xl text-xs"
                        />
                        <Input
                            type="text"
                            inputMode="decimal"
                            placeholder="Max $"
                            value={filters.maxPrice}
                            onChange={(e) => updatePriceFilter("maxPrice", e.target.value)}
                            className="bg-zinc-950/80 border-zinc-800 text-white placeholder:text-zinc-500 rounded-xl text-xs"
                        />
                    </div>

                    {/* Sorting & Clear */}
                    <div className="flex items-center gap-2">
                        <select
                            value={sortBy}
                            onChange={(e) => setSortBy(e.target.value as any)}
                            className="flex-1 bg-zinc-950/80 border border-zinc-800 text-white text-xs font-semibold rounded-xl px-3 py-2 focus:outline-none"
                        >
                            <option value="featured">Sort: Featured</option>
                            <option value="price_asc">Price: Low to High</option>
                            <option value="price_desc">Price: High to Low</option>
                            <option value="name">Name: A-Z</option>
                        </select>

                        {(filters.componentSkuPrefix !== "ALL" || filters.minPrice || filters.maxPrice || searchQuery) && (
                            <Button
                                variant="ghost"
                                size="sm"
                                onClick={clearAllFilters}
                                className="rounded-xl text-zinc-400 hover:text-white hover:bg-zinc-800 text-xs px-2"
                            >
                                Reset
                            </Button>
                        )}
                    </div>
                </div>

                {/* Product Grid */}
                {loading ? (
                    <div className="py-24 text-center space-y-3">
                        <div className="w-10 h-10 border-3 border-orange-500 border-t-transparent rounded-full animate-spin mx-auto" />
                        <p className="text-sm font-semibold text-zinc-400">Loading catalog...</p>
                    </div>
                ) : paginated.length === 0 ? (
                    <div className="py-20 text-center rounded-3xl border border-zinc-800/80 bg-zinc-900/30 p-8 max-w-md mx-auto space-y-4">
                        <div className="w-12 h-12 rounded-full bg-zinc-800 text-zinc-400 flex items-center justify-center mx-auto">
                            <Search className="w-6 h-6" />
                        </div>
                        <h3 className="text-lg font-bold text-white">No products found</h3>
                        <p className="text-xs text-zinc-400">
                            Try adjusting your price range, search query, or selecting another component category.
                        </p>
                        <Button
                            onClick={clearAllFilters}
                            className="rounded-xl bg-orange-500 hover:bg-orange-600 text-white text-xs font-bold"
                        >
                            Clear All Filters
                        </Button>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 sm:gap-6 auto-rows-fr">
                        {paginated.map((product) => (
                            <ProductCard key={product.skuPrefix} product={product} variant="grid" />
                        ))}
                    </div>
                )}

                {/* Pagination */}
                {totalPages > 1 && (
                    <div className="flex items-center justify-center gap-3 pt-8 pb-4">
                        <Button
                            variant="outline"
                            size="sm"
                            className="rounded-xl border-zinc-800 bg-zinc-900 hover:bg-zinc-800 text-white text-xs font-bold disabled:opacity-30"
                            disabled={page === 0}
                            onClick={() => {
                                setPage((p) => Math.max(0, p - 1));
                                window.scrollTo({ top: 0, behavior: "smooth" });
                            }}
                        >
                            ← Previous
                        </Button>

                        <span className="text-xs font-bold text-zinc-400 px-2">
                            Page <span className="text-orange-400">{page + 1}</span> of {totalPages}
                        </span>

                        <Button
                            variant="outline"
                            size="sm"
                            className="rounded-xl border-zinc-800 bg-zinc-900 hover:bg-zinc-800 text-white text-xs font-bold disabled:opacity-30"
                            disabled={page + 1 >= totalPages}
                            onClick={() => {
                                setPage((p) => Math.min(totalPages - 1, p + 1));
                                window.scrollTo({ top: 0, behavior: "smooth" });
                            }}
                        >
                            Next →
                        </Button>
                    </div>
                )}
            </div>
        </div>
    );
}
