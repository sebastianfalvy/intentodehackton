import { createContext, useContext, useEffect, useState } from 'react';
import type { ReactNode } from 'react';
import { apiClient } from '../api/client';

interface User {
  id: string;
  displayName: string;
  email: string;
  teamCode: string;
  role: string;
}

interface AuthContextType {
  user: User | null;
  loading: boolean;
  login: (token: string, userData: User) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const restoreSession = async () => {
      const token = localStorage.getItem('tropelcare_token');
      if (!token) {
        setLoading(false);
        return;
      }

      try {
        const data = await apiClient('/auth/me');
        setUser(data.user);
      } catch (error) {
        localStorage.removeItem('tropelcare_token');
      } finally {
        setLoading(false);
      }
    };

    restoreSession();
  }, []);

  const login = (token: string, userData: User) => {
    localStorage.setItem('tropelcare_token', token);
    setUser(userData);
  };

  const logout = () => {
    localStorage.removeItem('tropelcare_token');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth debe usarse dentro de un AuthProvider');
  return context;
};