export default function AppShell({ children }) {
    return (
        <main className="page">
            <div className="shell">
                <div className="logo">geno</div>
                <div className="subtitle">CONTROL DE INGRESOS</div>
                {children}
            </div>
        </main>
    );
}