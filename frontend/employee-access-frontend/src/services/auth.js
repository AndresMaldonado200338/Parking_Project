import { loginApi } from './api'; //
// No necesitas importar 'toast' aquí

export const authService = {
  async login(userID, password) {
    try {
      const response = await loginApi.post('/authuser', { //
        userID: Number(userID), //
        password //
      });

      // Asumimos que una respuesta exitosa con token es un 2xx
      // y que response.data contiene el token y quizás un mensaje.
      if (response.data && response.data.token) { //
        return { success: true, data: response.data, message: response.data.message || 'Inicio de sesión exitoso.' };
      } else {
        // Si el backend responde con 2xx pero sin token, o con una estructura inesperada.
        return { success: false, message: response.data.message || 'Respuesta inesperada del servidor.' };
      }
    } catch (error) {
      console.error('Auth service error:', error);
      let errorMessage = 'Error de autenticación. Intente más tarde.';
      if (error.response && error.response.data && error.response.data.message) {
        // Si el backend envía un mensaje de error específico en un error HTTP (ej. 401, 400)
        errorMessage = error.response.data.message;
      } else if (error.message === 'Network Error') {
        errorMessage = 'Error de red. Asegúrese de que el servidor backend esté accesible.';
      } else if (error.message) {
        errorMessage = error.message;
      }
      return { success: false, message: errorMessage, errorDetail: error };
    }
  }
};