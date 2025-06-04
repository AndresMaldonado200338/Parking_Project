<template>
  <form @submit.prevent="handleLogin">
    <div>
      <label for="userID">Usuario (ID):</label>
      <input type="text" v-model="userID" id="userID" required />
    </div>
    <div>
      <label for="password">Contraseña:</label>
      <input type="password" v-model="password" id="password" required />
    </div>
    <button type="submit">Iniciar Sesión</button>
  </form>
</template>

<script setup>
import { ref } from 'vue';
import { authService } from '@/services/auth'; // Ajusta la ruta si es diferente
import { useAuthStore } from '@/services/storage'; // Ajusta la ruta si es diferente
import { useRouter } from 'vue-router';
import { useToast } from 'vue-toastification'; // ¡Importante!

const userID = ref('');
const password = ref('');

const toast = useToast(); // Obtén la instancia de toast
const router = useRouter();
const authStore = useAuthStore(); // Obtén la instancia del store

const handleLogin = async () => {
  if (!userID.value || !password.value) {
    toast.error('Por favor, ingrese usuario y contraseña.');
    return;
  }

  const result = await authService.login(userID.value, password.value);

  // Verifica que result no sea undefined y tenga la propiedad success
  if (result && result.success) {
    // Guarda el token y el userID en el store
    // Asegúrate que tu backend envía 'userID' en response.data si lo necesitas
    authStore.login(result.data.token, result.data.userID || userID.value); 
    toast.success(result.message || '¡Inicio de sesión exitoso!');
    router.push('/home'); // O la ruta a la que quieras redirigir
  } else {
    // Muestra el mensaje de error que viene del servicio
    toast.error(result ? result.message : 'Error desconocido al iniciar sesión.');
  }
};
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