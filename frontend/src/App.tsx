import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import { NavBar } from './components/NavBar';
import {AuthProvider, useAuth} from "./context/AuthContext.tsx";
import type { JSX } from 'react';
import 'primeflex/primeflex.css';

const PrivateRoute = ({ children }: { children: JSX.Element }) => {
    const { isAuthenticated } = useAuth();
    return isAuthenticated ? children : <Navigate to="/login" replace />;
};

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <div className="min-h-screen flex flex-column surface-ground">
                    <NavBar />
                    <div className="flex-grow-1">
                        <Routes>
                            <Route path="/login" element={<LoginPage />} />
                            <Route path="/dashboard" element={
                                <PrivateRoute>
                                    <DashboardPage />
                                </PrivateRoute>
                            } />
                            <Route path="*" element={<Navigate to="/dashboard" replace />} />
                        </Routes>
                    </div>
                </div>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;