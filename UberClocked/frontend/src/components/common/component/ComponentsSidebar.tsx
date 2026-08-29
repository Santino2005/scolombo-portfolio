import { useMemo } from "react";
import { Check, Layers, Cpu, Flame, MemoryStick, HardDrive, Zap, Fan, Monitor, MousePointer } from "lucide-react";
import { Card } from "@/components/ui/card";
import type { ComponentDto } from "@/services/component";

type Props = {
    components: ComponentDto[];
    selectedSku: string;
    onSelect: (skuPrefix: string) => void;
    counts?: Record<string, number>;
    selectedByComponent?: Record<string, any>;
};

const ICONS_MAP: Record<string, any> = {
    CPU: Cpu,
    GPU: Flame,
    MOTHERBOARD: Layers,
    RAM: MemoryStick,
    SD: HardDrive,
    CASE: Layers,
    PSU: Zap,
    COOLER: Fan,
    MONITOR: Monitor,
    PERIPHERAL: MousePointer,
};

export default function ComponentsSidebar({
    components,
    selectedSku,
    onSelect,
    counts,
    selectedByComponent = {},
}: Props) {
    const items = useMemo(() => components, [components]);

    const isSlotSelected = (skuPrefix: string) => {
        if (selectedByComponent[skuPrefix]) return true;
        // Check slots for RAM and SD
        if (skuPrefix === "RAM" || skuPrefix === "SD") {
            return Object.keys(selectedByComponent).some((k) => k.startsWith(`${skuPrefix}_`));
        }
        return false;
    };

    return (
        <Card className="w-full lg:w-80 shrink-0 rounded-2xl bg-zinc-950/90 border border-zinc-800/80 p-4 self-start shadow-xl">
            <div className="flex items-center justify-between pb-3 border-b border-zinc-850">
                <h2 className="text-base font-black text-white uppercase tracking-wider flex items-center gap-2">
                    <Layers className="w-4 h-4 text-orange-400" /> PC Components
                </h2>
                <span className="text-[11px] font-bold text-zinc-400">
                    {Object.keys(selectedByComponent).length} Selected
                </span>
            </div>

            <div className="mt-3 grid grid-cols-2 lg:grid-cols-1 gap-2">
                {items.map((c) => {
                    const isSelected = selectedSku === c.skuPrefix;
                    const hasSelectedProduct = isSlotSelected(c.skuPrefix);
                    const Icon = ICONS_MAP[c.skuPrefix] || Layers;

                    return (
                        <button
                            key={c.skuPrefix}
                            type="button"
                            onClick={() => onSelect(c.skuPrefix)}
                            className={`w-full rounded-xl border p-3 text-left transition-all relative flex items-center justify-between gap-2.5 ${
                                isSelected
                                    ? "bg-orange-500/10 border-orange-500 text-white shadow-md shadow-orange-500/10"
                                    : "bg-zinc-900/60 border-zinc-800/80 text-zinc-300 hover:bg-zinc-900 hover:text-white"
                            }`}
                        >
                            <div className="flex items-center gap-2.5 min-w-0">
                                <div
                                    className={`w-8 h-8 rounded-lg flex items-center justify-center shrink-0 ${
                                        isSelected
                                            ? "bg-orange-500 text-white"
                                            : hasSelectedProduct
                                            ? "bg-emerald-500/20 text-emerald-400 border border-emerald-500/30"
                                            : "bg-zinc-800 text-zinc-400"
                                    }`}
                                >
                                    <Icon className="w-4 h-4" />
                                </div>
                                <div className="min-w-0 flex-1">
                                    <div className="text-xs font-bold truncate leading-tight">{c.displayName}</div>
                                    <div className="text-[10px] text-zinc-500 truncate font-mono">{c.skuPrefix}</div>
                                </div>
                            </div>

                            {hasSelectedProduct && (
                                <div className="w-5 h-5 rounded-full bg-emerald-500/20 text-emerald-400 flex items-center justify-center shrink-0">
                                    <Check className="w-3 h-3" />
                                </div>
                            )}
                        </button>
                    );
                })}
            </div>
        </Card>
    );
}