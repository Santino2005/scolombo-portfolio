import { useAuth0 } from "@auth0/auth0-react";
import { useEffect, useMemo, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import {
    Cpu,
    Check,
    Layers,
    ShoppingCart,
    AlertCircle,
    CheckCircle2,
    Sparkles,
    SlidersHorizontal,
    X,
    ArrowRight
} from "lucide-react";
import ComponentsSidebar from "@/components/common/component/ComponentsSidebar";
import ProductList from "@/components/common/products/list/ProductList";
import ProductsSelectedPanel from "@/components/common/products/selected/panel/ProductSelectedPanel";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import { fetchWithAuth } from "@/services/api.ts";
import { addCartItem, getMyCart } from "@/services/Cart";
import { getComponents, type ComponentDto } from "@/services/component";
import { getProductsByComponentPrefix, getProductBySkuPublic } from "@/services/Product";
import type { Product } from "./types/Product";

const CUSTOM_PC_SKU = "CUSTOM_PC";
const CASE_COMPONENT_SKU = "CASE";
const RAM_SLOTS = 4;
const SD_SLOTS = 4;
const MULTI_BASES = new Set<string>(["RAM", "SD"]);

function slotKey(base: string, index1: number) {
    return `${base}_${index1}`;
}

function getProductSku(p: any) {
    return p?.skuPrefix ?? p?.sku ?? p?.id ?? "";
}

export default function BuilderPage() {
    const { itemId } = useParams();
    const isEditMode = !!itemId;
    const navigate = useNavigate();

    const { isAuthenticated, loginWithRedirect, getAccessTokenSilently } = useAuth0();

    const [components, setComponents] = useState<ComponentDto[]>([]);
    const [products, setProducts] = useState<Product[]>([]);
    const [selectedComponentSku, setSelectedComponentSku] = useState<string>("CPU");
    const [selectedByComponent, setSelectedByComponent] = useState<Record<string, Product>>({});
    const [componentCounts, setComponentCounts] = useState<Record<string, number>>({});
    const [adding, setAdding] = useState(false);
    const [loadingProducts, setLoadingProducts] = useState(false);

    // Modal state for Build Saved
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [mobileRigOpen, setMobileRigOpen] = useState(false);

    const returnTo = isEditMode ? `/build/${itemId}` : `/build`;
    const DRAFT_KEY = isEditMode ? `pc_draft_edit_${itemId}` : `pc_draft_new`;

    const selectedItems = useMemo(
        () => Object.entries(selectedByComponent).map(([key, product]) => ({ key, product })),
        [selectedByComponent]
    );

    const componentsPayload = useMemo(() => {
        const out: Record<string, string> = {};
        for (const [key, product] of Object.entries(selectedByComponent)) {
            out[key] = getProductSku(product);
        }
        return out;
    }, [selectedByComponent]);

    // Save draft in sessionStorage
    useEffect(() => {
        try {
            const draft = {
                selectedComponentSku,
                components: componentsPayload,
            };
            sessionStorage.setItem(DRAFT_KEY, JSON.stringify(draft));
        } catch {}
    }, [DRAFT_KEY, selectedComponentSku, componentsPayload]);

    // Restore draft if any
    useEffect(() => {
        if (Object.keys(selectedByComponent).length > 0) return;

        const raw = sessionStorage.getItem(DRAFT_KEY);
        if (!raw) return;

        let parsed: any = null;
        try {
            parsed = JSON.parse(raw);
        } catch {
            return;
        }

        const draftComponents: Record<string, string> = parsed?.components ?? {};
        const draftSelectedComponentSku: string = parsed?.selectedComponentSku ?? "";

        if (!draftComponents || Object.keys(draftComponents).length === 0) return;

        (async () => {
            const entries = await Promise.all(
                Object.entries(draftComponents).map(async ([key, sku]) => {
                    try {
                        const p = await getProductBySkuPublic(sku);
                        return [key, p] as const;
                    } catch {
                        return [key, null] as const;
                    }
                })
            );

            const next: Record<string, Product> = {};
            for (const [key, product] of entries) {
                if (product) next[key] = product;
            }

            if (Object.keys(next).length > 0) {
                setSelectedByComponent(next);
            }

            if (draftSelectedComponentSku) {
                setSelectedComponentSku((prev) => prev || draftSelectedComponentSku);
            }
        })();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [DRAFT_KEY]);

    // Fetch components list
    useEffect(() => {
        (async () => {
            try {
                const res = await fetch(`${(import.meta.env.VITE_API_URL as string) || "http://localhost:8080"}/components`);
                if (res.ok) {
                    const data = await res.json();
                    if (Array.isArray(data) && data.length > 0) {
                        setComponents(data);
                        if (!selectedComponentSku && data[0]?.skuPrefix) {
                            setSelectedComponentSku(data[0].skuPrefix);
                        }
                    }
                }
            } catch (e) {
                console.error("components fetch failed:", e);
            }
        })();
    }, [selectedComponentSku]);

    // Preload if edit mode
    useEffect(() => {
        if (!isAuthenticated || !isEditMode) return;

        (async () => {
            try {
                const token = await getAccessTokenSilently();
                const cart = await getMyCart(token);

                const item = (cart.items ?? []).find((it: any) => String(it.id) === String(itemId));
                if (!item) return;

                const comps: Record<string, string> = item.components ?? {};

                const entries = await Promise.all(
                    Object.entries(comps).map(async ([componentKey, sku]) => {
                        try {
                            const p = await getProductBySkuPublic(sku);
                            return [componentKey, p] as const;
                        } catch {
                            return [componentKey, null] as const;
                        }
                    })
                );

                const nextSelected: Record<string, Product> = {};
                for (const [componentKey, product] of entries) {
                    if (product) nextSelected[componentKey] = product;
                }

                setSelectedByComponent(nextSelected);

                if (nextSelected[CASE_COMPONENT_SKU]) {
                    setSelectedComponentSku(CASE_COMPONENT_SKU);
                }
            } catch (e) {
                console.error("failed to preload custom pc from cart item", e);
            }
        })();
    }, [isAuthenticated, isEditMode, itemId, getAccessTokenSilently]);

    // Load products for selected component
    useEffect(() => {
        if (!selectedComponentSku) {
            setProducts([]);
            return;
        }

        setLoadingProducts(true);
        (async () => {
            try {
                const data = await getProductsByComponentPrefix(selectedComponentSku);
                const filtered = data.filter((p: any) => (p.stock ?? 0) > 0);
                setProducts(filtered);
                setComponentCounts((prev) => ({ ...prev, [selectedComponentSku]: filtered.length }));
            } catch (err) {
                console.error(err);
                setProducts([]);
            } finally {
                setLoadingProducts(false);
            }
        })();
    }, [selectedComponentSku]);

    async function addSelectedProduct(product: Product) {
        if (!selectedComponentSku) return;

        setSelectedByComponent((prev) => {
            const next = { ...prev };

            if (MULTI_BASES.has(selectedComponentSku)) {
                const maxSlots = selectedComponentSku === "RAM" ? RAM_SLOTS : SD_SLOTS;

                let placed = false;
                for (let i = 1; i <= maxSlots; i++) {
                    const key = slotKey(selectedComponentSku, i);
                    if (!next[key]) {
                        next[key] = product;
                        placed = true;
                        break;
                    }
                }

                if (!placed) {
                    alert(`No free slot for ${selectedComponentSku}. Maximum is ${maxSlots} slots.`);
                }
                return next;
            }

            next[selectedComponentSku] = product;
            return next;
        });
    }

    async function handleSave() {
        if (!isAuthenticated) {
            await loginWithRedirect({
                appState: { returnTo: window.location.pathname },
            });
            return;
        }

        if (!selectedByComponent[CASE_COMPONENT_SKU]) {
            alert("Custom PC requires selecting a CASE (Chassis).");
            return;
        }

        setAdding(true);
        try {
            const token = await getAccessTokenSilently();

            if (isEditMode) {
                await replaceCartItemComponents(token, itemId!, componentsPayload);
                setShowSuccessModal(true);
            } else {
                await addCartItem(token, {
                    productSku: CUSTOM_PC_SKU,
                    quantity: 1,
                    components: componentsPayload,
                });

                sessionStorage.removeItem(DRAFT_KEY);
                setShowSuccessModal(true);
            }
        } catch (e) {
            console.error(e);
            alert("Could not save Custom PC.");
        } finally {
            setAdding(false);
        }
    }

    async function replaceCartItemComponents(token: string, itId: string, comps: Record<string, string>) {
        return fetchWithAuth(
            `${(import.meta.env.VITE_API_URL as string) || "http://localhost:8080"}/carts/me/items/${itId}/components/bulk`,
            token,
            {
                method: "PATCH",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ components: comps }),
            }
        );
    }

    const selectedComponentName = useMemo(() => {
        const found = components.find((c) => c.skuPrefix === selectedComponentSku);
        return found?.displayName ?? selectedComponentSku;
    }, [components, selectedComponentSku]);

    const canAddCustomPc = !!selectedByComponent[CASE_COMPONENT_SKU];

    const totalBuildPrice = useMemo(() => {
        return Object.values(selectedByComponent).reduce((acc, p) => acc + (Number(p.price) || 0), 0);
    }, [selectedByComponent]);

    return (
        <div className="w-full min-h-screen bg-zinc-950 text-white p-4 sm:p-6 lg:p-8">
            <div className="max-w-7xl mx-auto space-y-6">
                {/* Header Title */}
                <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 border-b border-zinc-800/80 pb-6">
                    <div>
                        <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-orange-500/10 text-orange-400 text-xs font-black uppercase tracking-widest mb-1.5">
                            <Cpu className="w-3.5 h-3.5" /> Rig Configurator
                        </div>
                        <h1 className="text-3xl sm:text-4xl font-black tracking-tight text-white">
                            {isEditMode ? "Edit Your Custom PC" : "Custom PC Builder"}
                        </h1>
                        <p className="text-sm text-zinc-400 mt-1">
                            Pick components to assemble your custom rig. Case is required to finalize the build.
                        </p>
                    </div>

                    {/* Mobile View Rig Summary Button */}
                    <div className="flex items-center gap-3 lg:hidden">
                        <Button
                            variant="outline"
                            className="rounded-xl border-orange-500/40 bg-zinc-900 text-white text-xs font-bold flex items-center gap-2"
                            onClick={() => setMobileRigOpen((v) => !v)}
                        >
                            <SlidersHorizontal className="w-3.5 h-3.5 text-orange-400" />
                            <span>View Build ({selectedItems.length}) • ${totalBuildPrice.toFixed(2)}</span>
                        </Button>
                    </div>
                </div>

                {/* Main 3-Column / Responsive 2-Column Layout */}
                <div className="flex flex-col lg:flex-row gap-6 items-start">
                    {/* Left: Component Categories Sidebar */}
                    <ComponentsSidebar
                        components={components}
                        selectedSku={selectedComponentSku}
                        onSelect={setSelectedComponentSku}
                        counts={componentCounts}
                        selectedByComponent={selectedByComponent}
                    />

                    {/* Center: Available Products for Selected Category */}
                    <div className="flex-1 space-y-4 min-w-0 w-full">
                        <div className="rounded-2xl bg-zinc-950/80 border border-zinc-800/80 p-4 flex flex-col sm:flex-row sm:items-center justify-between gap-3 shadow-md">
                            <div>
                                <h2 className="text-base sm:text-lg font-black text-white flex items-center gap-2">
                                    <span>Available Parts for:</span>
                                    <span className="text-orange-400 font-extrabold">{selectedComponentName}</span>
                                </h2>
                                <p className="text-xs text-zinc-400">
                                    {products.length} compatible {selectedComponentName.toLowerCase()} in stock
                                </p>
                            </div>

                            {(selectedComponentSku === "RAM" || selectedComponentSku === "SD") && (
                                <div className="text-xs font-bold text-amber-400 bg-amber-500/10 px-3 py-1.5 rounded-xl border border-amber-500/20 self-start sm:self-auto">
                                    Multi-Slot: Up to {selectedComponentSku === "RAM" ? RAM_SLOTS : SD_SLOTS} drives
                                </div>
                            )}
                        </div>

                        {loadingProducts ? (
                            <div className="py-20 text-center space-y-2">
                                <div className="w-8 h-8 border-2 border-orange-500 border-t-transparent rounded-full animate-spin mx-auto" />
                                <p className="text-xs text-zinc-400">Loading parts...</p>
                            </div>
                        ) : (
                            <ProductList products={products} onSelect={addSelectedProduct} />
                        )}
                    </div>

                    {/* Right: Selected Rig Build Summary Panel (Desktop) */}
                    <div className="hidden lg:block w-96 shrink-0 space-y-4 sticky top-24">
                        <ProductsSelectedPanel
                            selectedItems={selectedItems}
                            onRemove={(componentKey) => {
                                setSelectedByComponent((prev) => {
                                    const next = { ...prev };
                                    delete next[componentKey];
                                    return next;
                                });
                            }}
                            returnTo={returnTo}
                            builderState={{
                                components: componentsPayload,
                                selectedComponentSku,
                                isEditMode,
                                itemId,
                            }}
                        />

                        <Button
                            className="w-full py-6 rounded-2xl bg-gradient-to-r from-orange-500 via-amber-500 to-orange-600 hover:from-orange-600 hover:to-orange-700 text-white font-black text-sm uppercase tracking-wide shadow-xl shadow-orange-500/25 border border-orange-400/30 active:scale-[0.98] transition-all disabled:opacity-40 disabled:shadow-none"
                            onClick={handleSave}
                            disabled={adding || !canAddCustomPc}
                        >
                            {adding ? (
                                "Saving PC Build..."
                            ) : isEditMode ? (
                                "Save Rig Changes"
                            ) : (
                                <span className="flex items-center justify-center gap-2">
                                    <ShoppingCart className="w-4 h-4" /> Add Custom PC to Cart
                                </span>
                            )}
                        </Button>
                    </div>
                </div>

                {/* Mobile Drawer for Rig Build Summary */}
                {mobileRigOpen && (
                    <div className="lg:hidden fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex flex-col justify-end p-4">
                        <div className="bg-zinc-950 border border-zinc-800 rounded-3xl p-5 space-y-4 max-h-[85vh] overflow-y-auto">
                            <div className="flex items-center justify-between">
                                <h3 className="font-black text-white text-lg">Your Custom Rig</h3>
                                <button
                                    onClick={() => setMobileRigOpen(false)}
                                    className="p-1.5 rounded-lg bg-zinc-900 text-zinc-400 hover:text-white"
                                >
                                    <X className="w-5 h-5" />
                                </button>
                            </div>

                            <ProductsSelectedPanel
                                selectedItems={selectedItems}
                                onRemove={(componentKey) => {
                                    setSelectedByComponent((prev) => {
                                        const next = { ...prev };
                                        delete next[componentKey];
                                        return next;
                                    });
                                }}
                            />

                            <Button
                                className="w-full py-6 rounded-2xl bg-orange-500 hover:bg-orange-600 text-white font-black text-sm uppercase tracking-wide shadow-lg"
                                onClick={() => {
                                    setMobileRigOpen(false);
                                    handleSave();
                                }}
                                disabled={adding || !canAddCustomPc}
                            >
                                {adding ? "Saving..." : isEditMode ? "Save Changes" : "Add Custom PC to Cart"}
                            </Button>
                        </div>
                    </div>
                )}

                {/* Build Success Popup Modal */}
                <Dialog open={showSuccessModal} onOpenChange={setShowSuccessModal}>
                    <DialogContent className="sm:max-w-md bg-zinc-950 border border-orange-500/40 text-white shadow-2xl p-6 rounded-3xl">
                        <DialogHeader className="text-center space-y-2">
                            <div className="mx-auto w-14 h-14 rounded-full bg-gradient-to-br from-emerald-500 to-teal-600 flex items-center justify-center shadow-lg shadow-emerald-500/25">
                                <CheckCircle2 className="w-8 h-8 text-white" />
                            </div>
                            <DialogTitle className="text-2xl font-black text-white">
                                {isEditMode ? "PC Build Updated! 🎉" : "Custom PC Added to Cart! 🚀"}
                            </DialogTitle>
                            <DialogDescription className="text-zinc-400 text-xs sm:text-sm">
                                {isEditMode
                                    ? "Your PC configuration changes have been saved."
                                    : "Your custom rig has been assembled and placed in your active cart."}
                            </DialogDescription>
                        </DialogHeader>

                        <div className="space-y-4 my-2">
                            <div className="bg-zinc-900/90 rounded-2xl border border-zinc-800 p-4 space-y-2 text-xs">
                                <div className="flex justify-between font-bold text-zinc-400">
                                    <span>Total Parts:</span>
                                    <span className="text-white">{selectedItems.length} Components</span>
                                </div>
                                <div className="flex justify-between font-bold text-zinc-400">
                                    <span>Rig Price:</span>
                                    <span className="text-orange-400 text-sm font-mono">${totalBuildPrice.toFixed(2)}</span>
                                </div>
                            </div>

                            <div className="grid grid-cols-2 gap-3 pt-2">
                                <Button
                                    variant="outline"
                                    className="rounded-xl border-zinc-700 bg-zinc-900 text-white text-xs font-bold"
                                    onClick={() => {
                                        setShowSuccessModal(false);
                                        setSelectedByComponent({});
                                    }}
                                >
                                    Build Another Rig
                                </Button>

                                <Button
                                    asChild
                                    className="rounded-xl bg-orange-500 hover:bg-orange-600 text-white text-xs font-bold flex items-center justify-center gap-1.5"
                                >
                                    <Link to="/cart">
                                        Go to Cart <ArrowRight className="w-3.5 h-3.5" />
                                    </Link>
                                </Button>
                            </div>
                        </div>
                    </DialogContent>
                </Dialog>
            </div>
        </div>
    );
}