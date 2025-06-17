import {Button} from 'primereact/button';
import {useNavigate} from 'react-router-dom';
import {useAuth} from "../context/AuthContext.tsx";

export const NavBar = () => {
    const {isAuthenticated, logout} = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        fetch('/api/logout', {method: 'POST', credentials: 'include'})
            .then(() => {
                logout();
                navigate('/login');
            });
    };

    return (
        <div className="w-full p-3 shadow-2">
            <div className="container mx-auto px-4">
                <div className="flex justify-content-between align-items-center">
                    <div className="font-bold" style={{fontSize: '1.5rem'}}>Electricity Consumption App</div>
                    <div>
                        {isAuthenticated ? (
                            <Button label="Logout" icon="pi pi-sign-out" onClick={handleLogout}/>
                        ) : (
                            <Button label="Login" icon="pi pi-sign-in" onClick={() => navigate('/login')}/>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};
