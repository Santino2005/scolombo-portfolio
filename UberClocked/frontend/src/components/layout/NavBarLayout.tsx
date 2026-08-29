import { Outlet, Link } from "react-router-dom";
import { Flame, ShieldCheck, Heart, Github } from "lucide-react";
import NavBar from "../common/navbar/NavBar";

function NavBarLayout() {
    return (
        <div className="w-full min-h-screen flex flex-col bg-zinc-950 text-white">
            <header className="shrink-0 pointer-events-auto">
                <NavBar />
            </header>

            <main className="flex-1 overflow-y-auto">
                <Outlet />
            </main>

            <footer className="shrink-0 text-xs text-zinc-400 py-8 px-4 sm:px-8 border-t border-zinc-900 bg-zinc-950">
                <div className="max-w-7xl mx-auto flex flex-col sm:flex-row items-center justify-between gap-4">
                    <div className="flex items-center gap-2">
                        <Flame className="w-4 h-4 text-orange-500" />
                        <span className="font-extrabold text-white">Über<span className="text-orange-500">Clocked</span></span>
                        <span className="text-zinc-600">|</span>
                        <span>Next-Gen High Performance PC Hardware</span>
                    </div>

                    <div className="flex items-center gap-4 text-zinc-400">
                        <Link to="/market" className="hover:text-orange-400 transition">Market</Link>
                        <Link to="/build" className="hover:text-orange-400 transition">PC Builder</Link>
                        <Link to="/posts" className="hover:text-orange-400 transition">Community</Link>
                        <Link to="/coupons" className="hover:text-orange-400 transition">Coupons</Link>
                    </div>

                    <div className="flex items-center gap-1.5 text-zinc-500 text-[11px]">
                        <span>© {new Date().getFullYear()} ÜberClocked Inc. All rights reserved.</span>
                    </div>
                </div>
            </footer>
        </div>
    );
}

export default NavBarLayout;
