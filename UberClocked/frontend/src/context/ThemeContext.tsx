import React, { createContext, useContext, useEffect, useState } from "react";

export type AccentColor = "orange" | "cyan" | "emerald" | "violet" | "rose" | "amber";
export type ThemeMode = "dark" | "light";

interface ThemeContextType {
    mode: ThemeMode;
    accent: AccentColor;
    setMode: (mode: ThemeMode) => void;
    setAccent: (accent: AccentColor) => void;
    toggleMode: () => void;
}

const ACCENT_CONFIG: Record<
    AccentColor,
    { name: string; primaryHsl: string; hex: string; gradient: string }
> = {
    orange: {
        name: "Über Flame (Orange)",
        primaryHsl: "24 95% 53%",
        hex: "#f97316",
        gradient: "from-orange-500 to-amber-500",
    },
    cyan: {
        name: "Electric Ice (Cyan)",
        primaryHsl: "189 94% 43%",
        hex: "#06b6d4",
        gradient: "from-cyan-500 to-blue-500",
    },
    emerald: {
        name: "Matrix Pro (Emerald)",
        primaryHsl: "160 84% 39%",
        hex: "#10b981",
        gradient: "from-emerald-500 to-teal-500",
    },
    violet: {
        name: "Hyper RGB (Violet)",
        primaryHsl: "270 95% 65%",
        hex: "#a855f7",
        gradient: "from-purple-500 to-pink-500",
    },
    rose: {
        name: "Crimson ROG (Rose)",
        primaryHsl: "350 89% 60%",
        hex: "#f43f5e",
        gradient: "from-rose-500 to-red-600",
    },
    amber: {
        name: "Overclock Gold (Amber)",
        primaryHsl: "45 93% 47%",
        hex: "#eab308",
        gradient: "from-amber-400 to-yellow-500",
    },
};

export { ACCENT_CONFIG };

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export function ThemeProvider({ children }: { children: React.ReactNode }) {
    const [mode, setModeState] = useState<ThemeMode>(() => {
        const saved = localStorage.getItem("uberclocked_theme_mode");
        return (saved === "light" || saved === "dark" ? saved : "dark") as ThemeMode;
    });

    const [accent, setAccentState] = useState<AccentColor>(() => {
        const saved = localStorage.getItem("uberclocked_theme_accent");
        return (saved && saved in ACCENT_CONFIG ? saved : "orange") as AccentColor;
    });

    const setMode = (newMode: ThemeMode) => {
        setModeState(newMode);
        localStorage.setItem("uberclocked_theme_mode", newMode);
    };

    const setAccent = (newAccent: AccentColor) => {
        setAccentState(newAccent);
        localStorage.setItem("uberclocked_theme_accent", newAccent);
    };

    const toggleMode = () => {
        setMode(mode === "dark" ? "light" : "dark");
    };

    useEffect(() => {
        const root = document.documentElement;

        // Apply dark/light class
        if (mode === "dark") {
            root.classList.add("dark");
            root.classList.remove("light");
        } else {
            root.classList.add("light");
            root.classList.remove("dark");
        }

        // Apply dynamic accent color
        const config = ACCENT_CONFIG[accent] || ACCENT_CONFIG.orange;
        root.style.setProperty("--primary", `hsl(${config.primaryHsl})`);
        root.style.setProperty("--primary-hex", config.hex);
    }, [mode, accent]);

    return (
        <ThemeContext.Provider value={{ mode, accent, setMode, setAccent, toggleMode }}>
            {children}
        </ThemeContext.Provider>
    );
}

export function useTheme() {
    const ctx = useContext(ThemeContext);
    if (!ctx) {
        throw new Error("useTheme must be used within ThemeProvider");
    }
    return ctx;
}
