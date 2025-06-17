import React, {useState} from 'react';
import {InputText} from 'primereact/inputtext';
import {Password} from 'primereact/password';
import {Button} from 'primereact/button';
import {useNavigate} from 'react-router-dom';
import {useAuth} from '../context/AuthContext';

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const {login} = useAuth();
    const navigate = useNavigate();

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        fetch('/api/login', {
            method: 'POST',
            credentials: 'include',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: new URLSearchParams({username, password}),
        }).then(res => {
            if (res.ok) {
                login();
                navigate('/dashboard');
            } else {
                setError('Invalid credentials');
            }
        }).catch(() => {
            setError('Network error');
        });
    };

    return (
        <div className="flex justify-content-center align-items-center" style={{height: '80vh'}}>
            <form onSubmit={handleSubmit} className="surface-card border-round shadow-3 p-4 mt-5"
                  style={{width: '400px'}}>
                <h2 className="mb-4">Login</h2>
                <div className="field flex flex-column gap-3">
                    <label htmlFor="username">Username</label>
                    <InputText id="username"
                               placeholder="Enter your username"
                               value={username}
                               onChange={e => setUsername(e.target.value)}
                               required
                    />
                </div>
                <div className="field flex flex-column gap-3">
                    <label htmlFor="password">Password</label>
                    <Password id="password"
                              placeholder="Enter your password"
                              value={password}
                              onChange={e => setPassword(e.target.value)}
                              feedback={false}
                              required
                              className="w-full"
                              toggleMask
                    />
                </div>
                {error && <small className="text-red-500">{error}</small>}
                <Button label="Login" icon="pi pi-sign-in" type="submit" className="w-full mt-3"/>
            </form>
        </div>
    );
};

export default LoginPage;