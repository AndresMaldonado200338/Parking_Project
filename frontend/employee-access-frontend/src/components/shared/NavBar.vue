<template>
  <header class="navbar">
    <div class="navbar-brand">
      <h1>Sistema de Control de Acceso</h1>
    </div>
    <div class="navbar-actions">
      <button @click="handleLogout" class="btn-logout">Cerrar Sesión</button>
    </div>
  </header>
</template>

<script>
import { useAuthStore } from '../../services/storage'
import { useRouter } from 'vue-router'
import { toast } from 'vue-toastification'

export default {
  setup() {
    const authStore = useAuthStore()
    const router = useRouter()

    const handleLogout = () => {
      authStore.logout()
      toast.success('Sesión cerrada correctamente')
      router.push('/')
    }

    return { handleLogout }
  }
}
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: #2c3e50;
  color: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.navbar-brand h1 {
  margin: 0;
  font-size: 1.5rem;
}

.btn-logout {
  padding: 0.5rem 1rem;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-logout:hover {
  background-color: #c0392b;
}
</style>