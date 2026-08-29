import { Palette, Sun, Moon, Check } from "lucide-react";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useTheme, ACCENT_CONFIG, type AccentColor } from "@/context/ThemeContext";

export default function ThemeSwitcher({ compact = false }: { compact?: boolean }) {
    const { mode, accent, setAccent, toggleMode } = useTheme();

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <button
                    type="button"
                    className="p-2 rounded-xl bg-zinc-900/90 border border-zinc-800 hover:border-orange-500/50 hover:bg-zinc-800 text-zinc-300 hover:text-white transition flex items-center gap-1.5 shadow-sm"
                    title="Change Theme & Accent Color"
                    aria-label="Theme Switcher"
                >
                    <Palette className="w-4 h-4 text-orange-400" />
                    {!compact && (
                        <span
                            className="w-2.5 h-2.5 rounded-full border border-white/30 hidden sm:inline-block"
                            style={{ backgroundColor: ACCENT_CONFIG[accent]?.hex }}
                        />
                    )}
                </button>
            </DropdownMenuTrigger>

            <DropdownMenuContent
                align="end"
                className="w-56 bg-zinc-950/95 border border-zinc-800 text-white shadow-2xl p-2 rounded-2xl backdrop-blur-xl z-50"
            >
                <DropdownMenuLabel className="text-xs font-black uppercase tracking-wider text-zinc-400 px-2 py-1 flex items-center justify-between">
                    <span>Accent Theme</span>
                    <button
                        type="button"
                        onClick={toggleMode}
                        className="p-1 rounded-lg hover:bg-zinc-800 text-zinc-300 hover:text-white transition flex items-center gap-1 text-[11px]"
                        title="Toggle Dark / Light"
                    >
                        {mode === "dark" ? (
                            <>
                                <Sun className="w-3.5 h-3.5 text-amber-400" /> Light
                            </>
                        ) : (
                            <>
                                <Moon className="w-3.5 h-3.5 text-blue-400" /> Dark
                            </>
                        )}
                    </button>
                </DropdownMenuLabel>

                <DropdownMenuSeparator className="bg-zinc-800" />

                <div className="grid grid-cols-2 gap-1.5 p-1">
                    {(Object.keys(ACCENT_CONFIG) as AccentColor[]).map((key) => {
                        const item = ACCENT_CONFIG[key];
                        const isSelected = accent === key;

                        return (
                            <button
                                key={key}
                                type="button"
                                onClick={() => setAccent(key)}
                                className={`flex items-center gap-2 p-2 rounded-xl text-xs font-bold transition-all text-left ${
                                    isSelected
                                        ? "bg-zinc-800 border border-white/20 text-white shadow"
                                        : "hover:bg-zinc-900/80 text-zinc-400 hover:text-white"
                                }`}
                            >
                                <span
                                    className="w-3.5 h-3.5 rounded-full shrink-0 shadow-sm flex items-center justify-center"
                                    style={{ backgroundColor: item.hex }}
                                >
                                    {isSelected && <Check className="w-2.5 h-2.5 text-white drop-shadow" />}
                                </span>
                                <span className="truncate text-[11px] capitalize">{key}</span>
                            </button>
                        );
                    })}
                </div>
            </DropdownMenuContent>
        </DropdownMenu>
    );
}
