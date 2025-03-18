import React from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import {useStore} from '../../store';
import './Auth.scss';


interface LoginFormInputs {
  username: string;
  password: string;
}

interface LoginProps {
  onSwitch: () => void;
}

const Login = ({ onSwitch }: LoginProps) => {
  const { register, handleSubmit, formState: { errors } } = useForm<LoginFormInputs>();
  const login = useStore((state) => state.login);
  const navigate = useNavigate();

  const onSubmit = async (data: LoginFormInputs) => {
    await login(data.username, data.password);
    const token = localStorage.getItem("token");
    if (token) {
      navigate('/dashboard');
    }
  };

  return (
    <div className="auth-container">
      <p className="auth-title">Đăng nhập</p>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="auth-form-group">
          <TextField
            {...register('username', { required: 'Tên đăng nhập là bắt buộc' })}
            label="Tên đăng nhập"
            placeholder="Tên đăng nhập"
            error={!!errors.username}
            helperText={errors.username ? errors.username.message : ''}
            fullWidth
          />
        </div>
        <div className="auth-form-group">
          <TextField
            type="password"
            {...register('password', { required: 'Mật khẩu là bắt buộc' })}
            label="Mật khẩu"
            placeholder="Mật khẩu"
            error={!!errors.password}
            helperText={errors.password ? errors.password.message : ''}
            fullWidth
          />
        </div>
        <p 
          className="auth-forgot-password-link"
          onClick={() => alert("Chức năng quên mật khẩu chưa được triển khai!")}
        >
          Quên mật khẩu?
        </p>
        <Button type="submit" variant="contained" className="auth-submit-button" fullWidth>
          Đăng nhập
        </Button>
      </form>
      <div className="auth-link-container">
        Cần một tài khoản? <span className="auth-switch-link" onClick={onSwitch}>Đăng ký</span>
      </div>
    </div>
  );
};

export default Login;
