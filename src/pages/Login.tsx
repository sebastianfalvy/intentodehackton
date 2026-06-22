import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { apiClient } from '../api/client';

export default function Login() {
  const [teamCode, setTeamCode] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');

    try {
      const data = await apiClient('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ teamCode, email: 'operator@tuckersoft.com', password })
      });
      login(data.token, data.user);
      navigate('/dashboard');
    } catch (err: any) {
      setError(err.message || 'Credenciales inválidas');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
      <form onSubmit={handleSubmit} className="bg-gray-800 p-8 rounded-lg shadow-xl w-96">
        <h2 className="text-2xl font-bold mb-6 text-center text-emerald-400">TropelCare Control</h2>
        {error && <div className="bg-red-500 text-white p-2 mb-4 rounded">{error}</div>}
        
        <label className="block mb-4">
          <span className="text-gray-300">Team Code</span>
          <input type="text" required value={teamCode} onChange={(e) => setTeamCode(e.target.value)} 
                 className="mt-1 w-full p-2 rounded bg-gray-700 border border-gray-600 focus:border-emerald-500 outline-none" />
        </label>
        
        <label className="block mb-6">
          <span className="text-gray-300">Password</span>
          <input type="password" required value={password} onChange={(e) => setPassword(e.target.value)} 
                 className="mt-1 w-full p-2 rounded bg-gray-700 border border-gray-600 focus:border-emerald-500 outline-none" />
        </label>
        
        <button type="submit" disabled={isLoading} 
                className="w-full bg-emerald-600 hover:bg-emerald-500 p-2 rounded font-bold transition-colors disabled:opacity-50">
          {isLoading ? 'Iniciando...' : 'Entrar a Consola'}
        </button>
      </form>
    </div>
  );
}