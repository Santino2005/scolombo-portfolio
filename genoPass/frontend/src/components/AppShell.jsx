import { APP_CONFIG } from "../constants/config";

export default function AppShell({ children, wide = false }) {
    return (
        <main className="page">
            <div className={`shell ${wide ? "shell-wide" : ""}`}>
                <header className="brand-header">
                    <div className="brand-badge">
                        <span className="brand-logo-text">geno</span>
                        <span className="brand-dot"></span>
                    </div>
                    <h1 className="brand-title">{APP_CONFIG.APP_NAME}</h1>
                    <p className="brand-subtitle">{APP_CONFIG.APP_SUBTITLE}</p>
                </header>
                {children}
            </div>
        </main>
    );
}
