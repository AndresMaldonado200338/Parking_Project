<template>
  <!-- Este componente es opcional ya que vue-toastification provee su propio componente -->
  <!-- Pero puedes usarlo para notificaciones personalizadas dentro de tu aplicación -->
  <div class="toast-notification" :class="type">
    <div class="toast-content">
      <span class="toast-icon" v-if="icon">
        <i :class="iconClass"></i>
      </span>
      <div class="toast-message">
        <h4 v-if="title" class="toast-title">{{ title }}</h4>
        <p class="toast-text"><slot></slot></p>
      </div>
      <button v-if="dismissible" class="toast-close" @click="$emit('close')">
        &times;
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ToastNotification',
  props: {
    type: {
      type: String,
      default: 'info',
      validator: (value) => ['success', 'error', 'warning', 'info'].includes(value)
    },
    title: {
      type: String,
      default: ''
    },
    icon: {
      type: Boolean,
      default: true
    },
    dismissible: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    iconClass() {
      const icons = {
        success: 'fas fa-check-circle',
        error: 'fas fa-exclamation-circle',
        warning: 'fas fa-exclamation-triangle',
        info: 'fas fa-info-circle'
      }
      return icons[this.type]
    }
  }
}
</script>

<style scoped>
.toast-notification {
  display: flex;
  max-width: 350px;
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: var(--radius);
  box-shadow: var(--shadow-md);
  background-color: var(--white);
  color: var(--text-color);
  transition: var(--transition);
  overflow: hidden;
}

.toast-content {
  display: flex;
  align-items: center;
  width: 100%;
}

.toast-icon {
  font-size: 1.5rem;
  margin-right: 1rem;
}

.toast-message {
  flex: 1;
}

.toast-title {
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 0.25rem;
}

.toast-text {
  font-size: 0.875rem;
  margin: 0;
}

.toast-close {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  padding: 0 0 0 1rem;
  color: inherit;
  opacity: 0.7;
  transition: opacity 0.3s;
}

.toast-close:hover {
  opacity: 1;
}

/* Type styles */
.toast-notification.success {
  background-color: var(--secondary-color);
  color: white;
}

.toast-notification.error {
  background-color: var(--danger-color);
  color: white;
}

.toast-notification.warning {
  background-color: var(--warning-color);
  color: white;
}

.toast-notification.info {
  background-color: var(--primary-color);
  color: white;
}
</style>