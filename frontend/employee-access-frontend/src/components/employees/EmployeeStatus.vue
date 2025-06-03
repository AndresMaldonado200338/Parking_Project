<template>
  <div class="toast-notification">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'ToastNotification'
}
</script>

<style scoped>
.toast-notification {
  padding: 0.75rem 1rem;
  border-radius: var(--radius);
  background-color: var(--bg-color);
  box-shadow: var(--shadow);
  max-width: 350px;
  margin-bottom: 0.5rem;
}

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