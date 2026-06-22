import { useEffect, useState } from 'react';
import { apiClient } from '../api/client';
import { useAuth } from '../context/AuthContext';

interface DashboardSummary {
  totalTropels: number;
  criticalTropels: number;
  openSignals: number;
  sectorStabilityAvg: number;
}

export default function Dashboard() {
  const { user, logout } = useAuth();
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiClient('/dashboard/summary')
      .then(data => setSummary(data))
      .catch(err => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="min-h-screen flex items-center justify-center bg-gray-900 text-emerald-400">Cargando métricas...</div>;
  if (error) return <div className="min-h-screen flex items-center justify-center bg-gray-900 text-red-400">Error: {error}</div>;

  return (
    <div className="min-h-screen bg-gray-900 text-gray-100 p-8">
      <header className="flex justify-between items-center mb-8 border-b border-gray-700 pb-4">
        <h1 className="text-3xl font-bold text-emerald-400">Dashboard de Operaciones</h1>
        <div className="flex items-center gap-4">
          <span className="text-gray-400">Operador: {user?.displayName} ({user?.teamCode})</span>
          <button onClick={logout} className="bg-red-600/20 text-red-400 hover:bg-red-600 hover:text-white px-4 py-2 rounded transition-colors">
            Cerrar Sesión
          </button>
        </div>
      </header>

      {summary ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <StatCard title="Total Tropeles" value={summary.totalTropels} />
          <StatCard title="Tropeles Críticos" value={summary.criticalTropels} isCritical={summary.criticalTropels > 10} />
          <StatCard title="Señales Abiertas" value={summary.openSignals} />
          <StatCard title="Estabilidad Promedio" value={`${summary.sectorStabilityAvg}%`} />
        </div>
      ) : (
        <div className="text-center text-gray-500 py-20">No hay datos disponibles en el sector.</div>
      )}
    </div>
  );
}

const StatCard = ({ title, value, isCritical = false }: { title: string, value: string | number, isCritical?: boolean }) => (
  <div className={`p-6 rounded-lg border ${isCritical ? 'bg-red-900/20 border-red-500/50' : 'bg-gray-800 border-gray-700'}`}>
    <h3 className="text-gray-400 text-sm font-semibold uppercase tracking-wider mb-2">{title}</h3>
    <p className={`text-4xl font-bold ${isCritical ? 'text-red-400' : 'text-gray-100'}`}>{value}</p>
  </div>
);