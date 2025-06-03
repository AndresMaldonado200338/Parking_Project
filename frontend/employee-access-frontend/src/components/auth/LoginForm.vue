<template>
  <div class="login-form">
    <h2>Iniciar Sesión</h2>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="userID">ID de Usuario</label>
        <input
          type="text"
          id="userID"
          v-model="userID"
          placeholder="Ingrese su ID"
          required
        />
      </div>
      <div class="form-group">
        <label for="password">Contraseña</label>
        <input
          type="password"
          id="password"
          v-model="password"
          placeholder="Ingrese su contraseña"
          required
        />
      </div>
      <button type="submit" class="btn-login">Ingresar</button>
    </form>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '../../services/auth'
import { useAuthStore } from '../../services/storage'
import { toast } from 'vue-toastification'

export default {
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    const userID = ref('')
    const password = ref('')

    const handleLogin = async () => {
      const result = await authService.login(userID.value, password.value)
      if (result && result.token) {
        authStore.login(result.token, userID.value)
        toast.success('Bienvenido')
        router.push('/home')
      }
    }

    return { userID, password, handleLogin }
  }
}
</script>

<style scoped>
.login-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #2c3e50;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #34495e;
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.btn-login {
  width: 100%;
  padding: 0.75rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-login:hover {
  background-color: #2980b9;
}
</style>