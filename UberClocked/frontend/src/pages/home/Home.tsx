import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useLoaderData } from "react-router-dom";
import { useAuth0 } from "@auth0/auth0-react";
import {
    Cpu,
    Zap,
    ShieldCheck,
    Truck,
    Sparkles,
    ArrowRight,
    Flame,
    Layers,
    Monitor,
    MousePointer,
    HardDrive,
    MemoryStick,
    Fan,
    CheckCircle2,
    RefreshCw,
    AlertCircle
} from "lucide-react";
import { Button } from "@/components/ui/button";
import ProductCarousel from "@/components/common/products/carousel/ProductCarousel";
import DiscountWheel from "@/components/common/wheel/DiscountWheel";
import { getWheelStatus, spinWheel } from "@/services/wheelApi";
import type { Props } from "./Home.types";

const CATEGORIES = [
    { name: "Processors", sku: "CPU", icon: Cpu, desc: "Intel Core & AMD Ryzen", count: "21 Models" },
    { name: "Graphics Cards", sku: "GPU", icon: Flame, desc: "RTX 40 Series & RX 7000", count: "21 Models" },
    { name: "Motherboards", sku: "MOTHERBOARD", icon: Layers, desc: "Z790, X670E, B650 & more", count: "21 Models" },
    { name: "RAM Memory", sku: "RAM", icon: MemoryStick, desc: "DDR5 up to 7000MHz", count: "21 Kits" },
    { name: "Fast Storage", sku: "SD", icon: HardDrive, desc: "PCIe 5.0 & Gen4 NVMe SSDs", count: "21 Drives" },
    { name: "Power Supplies", sku: "PSU", icon: Zap, desc: "80+ Gold/Platinum ATX 3.0", count: "21 Units" },
    { name: "Cases & Chassis", sku: "CASE", icon: Layers, desc: "Panoramic & Airflow Towers", count: "21 Cases" },
    { name: "Liquid Coolers", sku: "COOLER", icon: Fan, desc: "360mm AIOs & Quiet Towers", count: "21 Coolers" },
    { name: "Gaming Displays", sku: "MONITOR", icon: Monitor, desc: "OLED & 240Hz Fast IPS", count: "21 Monitors" },
    { name: "Pro Peripherals", sku: "PERIPHERAL", icon: MousePointer, desc: "Esports Mice & Keyboards", count: "21 Items" },
];

export default function Home() {
    const { products } = useLoaderData() as Props;
    const { isAuthenticated, loginWithRedirect, getAccessTokenSilently } = useAuth0();

    const [canSpin, setCanSpin] = useState<boolean | null>(null);
    const [secondsRemaining, setSecondsRemaining] = useState<number | null>(null);
    const [nextSpinAt, setNextSpinAt] = useState<string | null>(null);
    const [statusError, setStatusError] = useState<string | null>(null);
    const [loadingStatus, setLoadingStatus] = useState(false);

    async function refreshStatus() {
        if (!isAuthenticated) {
            setCanSpin(true);
            return;
        }

        setLoadingStatus(true);
        setStatusError(null);
        try {
            const token = await getAccessTokenSilently();
            const s = await getWheelStatus(() => Promise.resolve(token));
            setCanSpin(s.canSpin);
            setSecondsRemaining(s.secondsRemaining ?? null);
            setNextSpinAt(s.nextSpinAt ?? null);
        } catch (e: any) {
            setStatusError(String(e?.message ?? e));
            setCanSpin(false);
        } finally {
            setLoadingStatus(false);
        }
    }

    useEffect(() => {
        refreshStatus();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isAuthenticated]);

    const handleWheelSpin = async () => {
        if (!isAuthenticated) {
            await loginWithRedirect({
                appState: { returnTo: "/" },
            });
            throw new Error("Please log in to spin the wheel.");
        }

        const token = await getAccessTokenSilently();
        const res = await spinWheel(() => Promise.resolve(token));

        if (!res.canSpin) {
            setCanSpin(false);
            setNextSpinAt(res.nextSpinAt ?? null);
            await refreshStatus();
            throw new Error("You already spun today! Check back tomorrow.");
        }

        await refreshStatus();
        return res;
    };

    return (
        <div className="w-full min-h-screen text-white bg-gradient-to-b from-zinc-950 via-zinc-900 to-zinc-950">
            {/* Hero Section */}
            <section className="relative overflow-hidden pt-12 pb-20 lg:pt-20 lg:pb-28 border-b border-zinc-800/80">
                {/* Background Ambient Glows */}
                <div className="absolute top-1/4 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[350px] bg-gradient-to-tr from-orange-600/20 via-amber-500/20 to-purple-600/10 blur-[130px] pointer-events-none" />

                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
                    <div className="text-center max-w-4xl mx-auto space-y-6">
                        {/* Pill Badge */}
                        <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-zinc-900/90 border border-orange-500/30 text-orange-400 text-xs font-black uppercase tracking-widest shadow-lg shadow-orange-500/10 backdrop-blur-md animate-bounce">
                            <Flame className="w-4 h-4 text-orange-400" />
                            Next-Gen High Performance Hardware
                        </div>

                        {/* Hero Headline */}
                        <h1 className="text-4xl sm:text-6xl lg:text-7xl font-black tracking-tight leading-[1.08] text-white">
                            Overclock Your <span className="text-transparent bg-clip-text bg-gradient-to-r from-orange-400 via-amber-300 to-orange-500">Dream PC.</span>
                        </h1>

                        <p className="text-base sm:text-lg lg:text-xl text-zinc-300 max-w-2xl mx-auto font-medium">
                            Build custom computers with real-time compatibility checks, shop over 200+ flagship components, and unlock exclusive daily discounts.
                        </p>

                        {/* CTA Buttons */}
                        <div className="flex flex-col sm:flex-row items-center justify-center gap-3 pt-2">
                            <Button
                                asChild
                                size="lg"
                                className="w-full sm:w-auto px-8 py-6 rounded-2xl bg-gradient-to-r from-orange-500 via-amber-500 to-orange-600 hover:from-orange-600 hover:to-orange-700 text-white font-extrabold text-base shadow-xl shadow-orange-500/30 border border-orange-400/30 active:scale-95 transition-all"
                            >
                                <Link to="/build" className="flex items-center justify-center gap-2">
                                    <Cpu className="w-5 h-5" /> Start Custom PC Build
                                </Link>
                            </Button>

                            <Button
                                asChild
                                size="lg"
                                variant="outline"
                                className="w-full sm:w-auto px-8 py-6 rounded-2xl bg-zinc-900/90 hover:bg-zinc-800 border-zinc-700 text-white font-extrabold text-base shadow-md active:scale-95 transition-all"
                            >
                                <Link to="/market" className="flex items-center justify-center gap-2">
                                    Browse 200+ Products <ArrowRight className="w-4 h-4 text-orange-400" />
                                </Link>
                            </Button>
                        </div>

                        {/* Trust Highlights Grid */}
                        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 pt-10 border-t border-zinc-800/80 max-w-4xl mx-auto">
                            <div className="flex flex-col items-center p-3 rounded-xl bg-zinc-900/50 border border-zinc-800/60">
                                <span className="text-2xl font-black text-orange-400">200+</span>
                                <span className="text-xs text-zinc-400 font-semibold">Flagship Parts</span>
                            </div>
                            <div className="flex flex-col items-center p-3 rounded-xl bg-zinc-900/50 border border-zinc-800/60">
                                <span className="text-2xl font-black text-amber-400">100%</span>
                                <span className="text-xs text-zinc-400 font-semibold">Compatibility Check</span>
                            </div>
                            <div className="flex flex-col items-center p-3 rounded-xl bg-zinc-900/50 border border-zinc-800/60">
                                <span className="text-2xl font-black text-emerald-400">3-Year</span>
                                <span className="text-xs text-zinc-400 font-semibold">Direct Warranty</span>
                            </div>
                            <div className="flex flex-col items-center p-3 rounded-xl bg-zinc-900/50 border border-zinc-800/60">
                                <span className="text-2xl font-black text-purple-400">50%</span>
                                <span className="text-xs text-zinc-400 font-semibold">Daily Roulette Deals</span>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* Featured Deals Carousel */}
            <section className="py-16 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
                <ProductCarousel products={products} />
            </section>

            {/* Hardware Categories Grid */}
            <section className="py-16 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
                <div className="flex flex-col md:flex-row md:items-end justify-between mb-8">
                    <div>
                        <div className="text-xs font-black uppercase tracking-widest text-orange-400 flex items-center gap-1.5 mb-1">
                            <Layers className="w-4 h-4" /> Hardware Catalog
                        </div>
                        <h2 className="text-2xl sm:text-3xl font-black tracking-tight text-white">
                            Browse by Component Category
                        </h2>
                    </div>
                    <Link
                        to="/market"
                        className="text-xs font-bold text-orange-400 hover:text-orange-300 flex items-center gap-1 mt-2 md:mt-0 transition"
                    >
                        Explore all 200+ parts in Market <ArrowRight className="w-4 h-4" />
                    </Link>
                </div>

                <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3 sm:gap-4">
                    {CATEGORIES.map((cat) => {
                        const Icon = cat.icon;
                        return (
                            <Link
                                key={cat.sku}
                                to={`/market?component=${cat.sku}`}
                                className="group p-4 rounded-2xl bg-zinc-950/80 border border-zinc-800/80 hover:border-orange-500/50 hover:bg-zinc-900/90 transition-all duration-300 flex flex-col justify-between shadow-lg hover:shadow-orange-500/10"
                            >
                                <div className="space-y-3">
                                    <div className="w-10 h-10 rounded-xl bg-orange-500/10 border border-orange-500/20 text-orange-400 flex items-center justify-center group-hover:scale-110 group-hover:bg-orange-500 group-hover:text-white transition-all duration-300">
                                        <Icon className="w-5 h-5" />
                                    </div>
                                    <div>
                                        <h3 className="text-sm sm:text-base font-extrabold text-white group-hover:text-orange-400 transition leading-snug">
                                            {cat.name}
                                        </h3>
                                        <p className="text-[11px] text-zinc-400 line-clamp-1 mt-0.5">{cat.desc}</p>
                                    </div>
                                </div>
                                <div className="mt-3 pt-2 border-t border-zinc-900 flex items-center justify-between text-[10px] font-bold text-zinc-400">
                                    <span>{cat.count}</span>
                                    <span className="text-orange-400 group-hover:translate-x-1 transition-transform">→</span>
                                </div>
                            </Link>
                        );
                    })}
                </div>
            </section>

            {/* Daily Lucky Roulette Section */}
            <section className="py-20 border-y border-zinc-800/80 bg-zinc-950/60 relative overflow-hidden">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
                    <div className="text-center max-w-2xl mx-auto mb-10 space-y-3">
                        <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-amber-500/10 border border-amber-500/30 text-amber-300 text-xs font-black uppercase tracking-widest">
                            <Sparkles className="w-4 h-4 text-amber-400" /> Daily Gamer Lucky Wheel
                        </div>
                        <h2 className="text-3xl sm:text-4xl font-black tracking-tight text-white">
                            Spin Once a Day. Save on Every Build.
                        </h2>
                        <p className="text-sm text-zinc-300">
                            Coordinate your daily spin for instant coupon codes ranging from 5% up to 50% OFF, applicable to any hardware or custom rig!
                        </p>
                    </div>

                    <div className="max-w-md mx-auto">
                        <DiscountWheel
                            canSpin={canSpin === true}
                            secondsRemaining={secondsRemaining}
                            nextSpinAt={nextSpinAt}
                            onSpin={handleWheelSpin}
                        />

                        {statusError && (
                            <div className="mt-4 p-3 rounded-xl border border-red-500/30 bg-red-500/10 text-red-400 text-xs text-center flex items-center justify-center gap-2">
                                <AlertCircle className="w-4 h-4 shrink-0" />
                                <span>{statusError}</span>
                                <button onClick={refreshStatus} className="underline font-bold ml-2">
                                    Retry
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </section>

            {/* Custom PC Builder Teaser */}
            <section className="py-20 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
                <div className="rounded-3xl bg-gradient-to-r from-zinc-950 via-zinc-900 to-zinc-950 border border-zinc-800 p-8 sm:p-12 relative overflow-hidden shadow-2xl">
                    <div className="absolute top-0 right-0 w-96 h-96 bg-orange-500/10 blur-[100px] pointer-events-none" />

                    <div className="max-w-2xl space-y-6 relative z-10">
                        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-orange-500/10 text-orange-400 border border-orange-500/30 text-xs font-bold uppercase tracking-widest">
                            <Cpu className="w-4 h-4" /> PC Configurator
                        </div>

                        <h2 className="text-3xl sm:text-5xl font-black tracking-tight text-white">
                            Build Your Ultimate Battlestation.
                        </h2>

                        <p className="text-sm sm:text-base text-zinc-300 leading-relaxed">
                            Select matching processors, motherboards, DDR5 RAM, and liquid cooling with automated slot management, wattage calculations, and live pricing.
                        </p>

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 py-2 text-xs text-zinc-300 font-semibold">
                            <div className="flex items-center gap-2">
                                <CheckCircle2 className="w-4 h-4 text-orange-400 shrink-0" />
                                <span>4x RAM & 4x NVMe Slot Expansion</span>
                            </div>
                            <div className="flex items-center gap-2">
                                <CheckCircle2 className="w-4 h-4 text-orange-400 shrink-0" />
                                <span>Automatic Form-Factor Validation</span>
                            </div>
                            <div className="flex items-center gap-2">
                                <CheckCircle2 className="w-4 h-4 text-orange-400 shrink-0" />
                                <span>1-Click Custom PC Cart Addition</span>
                            </div>
                            <div className="flex items-center gap-2">
                                <CheckCircle2 className="w-4 h-4 text-orange-400 shrink-0" />
                                <span>Expert Rig Assembly & Testing</span>
                            </div>
                        </div>

                        <div className="pt-2">
                            <Button
                                asChild
                                size="lg"
                                className="px-8 py-6 rounded-2xl bg-orange-500 hover:bg-orange-600 text-white font-extrabold text-base shadow-xl shadow-orange-500/30 border border-orange-400/30"
                            >
                                <Link to="/build">Launch PC Configurator Now →</Link>
                            </Button>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
}