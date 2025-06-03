<template>
  <aside class="side-menu">
    <nav>
      <ul>
        <li>
          <router-link to="/home" active-class="active">Inicio</router-link>
        </li>
        <li>
          <router-link to="/employees" active-class="active">Empleados</router-link>
        </li>
        <li>
          <router-link to="/access" active-class="active">Control de Acceso</router-link>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<script>
export default {
  name: 'SideMenu'
}
</script>

<style scoped>
.side-menu {
  width: 250px;
  background-color: #34495e;
  color: white;
  height: 100vh;
  padding: 1rem 0;
}

nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

nav li a {
  display: block;
  padding: 1rem 2rem;
  color: white;
  text-decoration: none;
  transition: background-color 0.3s;
}

nav li a:hover {
  background-color: #2c3e50;
}

nav li a.active {
  background-color: #3498db;
}
</style>