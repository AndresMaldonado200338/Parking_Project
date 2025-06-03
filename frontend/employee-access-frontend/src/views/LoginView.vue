<template>
  <div class="login-view">
    <div class="login-container">
      <div class="login-header">
        <h1>Sistema de Control de Acceso</h1>
        <p>Gestor de empleados y registros de acceso</p>
      </div>
      <LoginForm />
    </div>
  </div>
</template>

<script>
import LoginForm from '../components/auth/LoginForm.vue'

export default {
  components: {
    LoginForm
  }
}
</script>

<style scoped>
.login-view {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #3498db, #2c3e50);
}

.login-container {
  width: 100%;
  max-width: 500px;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-header h1 {
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.login-header p {
  color: #7f8c8d;
}
</style>