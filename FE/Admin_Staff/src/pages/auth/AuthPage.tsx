import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import Login from './Login';
import Register from './Register';
import './Auth.scss';

const AuthPage = () => {
  const [searchParams] = useSearchParams();
  const initialIsLogin = searchParams.get('form') === 'register' ? false : true;
  const [isLogin, setIsLogin] = useState(initialIsLogin);

  useEffect(() => {
    const formParam = searchParams.get('form');
    if (formParam === 'register') {
      setIsLogin(false);
    } else {
      setIsLogin(true);
    }
  }, [searchParams]);

  return (
    <div className="auth-page">
      {isLogin ? (
        <Login onSwitch={() => setIsLogin(false)} />
      ) : (
        <Register onSwitch={() => setIsLogin(true)} />
      )}
    </div>
  );
};

export default AuthPage;
