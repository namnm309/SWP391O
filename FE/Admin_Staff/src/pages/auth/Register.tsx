import React from 'react';
import { useForm } from 'react-hook-form';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import { useStore } from '../../store';
import './Auth.scss';

interface RegisterFormInputs {
  username: string;
  email: string;
  phone: string;
  password: string;
}

interface RegisterProps {
  onSwitch: () => void;
}

const Register = ({ onSwitch }: RegisterProps) => {
  const { register, handleSubmit, formState: { errors } } = useForm<RegisterFormInputs>();
  const registerUser = useStore((state) => state.registerUser);

  const onSubmit = (data: RegisterFormInputs) => {
    registerUser({
      username: data.username,
      email: data.email,
      phone: data.phone,
      password: data.password,
    });
  };

  return (
    <div className="auth-container">
      <p className="auth-title">Đăng ký</p>
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
            {...register('email', { required: 'Email là bắt buộc' })}
            label="Email"
            placeholder="Email"
            error={!!errors.email}
            helperText={errors.email ? errors.email.message : ''}
            fullWidth
          />
        </div>
        <div className="auth-form-group">
          <TextField
            {...register('phone', { required: 'Số điện thoại là bắt buộc' })}
            label="Số điện thoại"
            placeholder="Số điện thoại"
            error={!!errors.phone}
            helperText={errors.phone ? errors.phone.message : ''}
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
        <Button type="submit" variant="contained" className="auth-submit-button" fullWidth>
          Đăng ký
        </Button>
      </form>
      <div className="auth-link-container">
        Đã có tài khoản? <span className="auth-switch-link" onClick={onSwitch}>Đăng nhập</span>
      </div>
    </div>
  );
};

export default Register;
