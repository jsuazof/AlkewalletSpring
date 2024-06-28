$(document).ready(function() {

  // validacion de las contraseñas sean iguales
  function validatePasswords() {
    var password = $("#password").val();
    var passwordConfirm = $("#passwordConfirm").val();
    
    if (password !== passwordConfirm) {
        alert("Las contraseñas no coinciden. Por favor, asegúrate de que las contraseñas sean iguales.");
        return false;
    }
    return true;
  }

  // Función para registrar un nuevo usuario
      $("#formCreateAccount").submit(function(e) {
          e.preventDefault();

          if (!validatePasswords()) {
              return;
          }

          var userData = {
              name: $("#name").val(),
              surname: $("#surname").val(),
              email: $("#email").val(),
              password: $("#password").val()
          };

          fetch("/register", {
              method: "POST",
              headers: {
                  "Content-Type": "application/json"
              },
              body: JSON.stringify(userData)
          })
          .then(response => {
              if (!response.ok) {
                  throw new Error("Error en la solicitud: " + response.status);
              }
              return response.text();
          })
          .then(data => {
              alert(data);
              window.location.href = "/index.html";
          })
          .catch(error => {
              alert("Error: " + error.message);
          });
      });

      $("#formLogin").submit(function(e) {
              e.preventDefault();

              var email = $("#emailLogin").val();
              var password = $("#passLogin").val();

              var userData = {
                  email: email,
                  password: password
              };

              // Enviar la solicitud POST al endpoint "/login" en el controlador
              fetch("/login", {
                  method: "POST",
                  headers: {
                      "Content-Type": "application/json"
                  },
                  body: JSON.stringify(userData)
              })
              .then(response => {
                  if (!response.ok) {
                      throw new Error("Error en la solicitud: " + response.status);
                  }
                  return response.json();
              })
              .then(data => {
                  // Procesar la respuesta del backend
                  if (data) {
                      // La autenticación fue exitosa
                      alert("Inicio de sesión exitoso");
                      window.location.href = "/home.html";
                  } else {
                      // La autenticación falló
                      alert("Credenciales incorrectas");
                  }
              })
              .catch(error => {
                  alert("Error: " + error.message);
              });
          });

    // para cambiar en pantalla de iniciar sesion a crear cuenta en el mismo contendor(Pc)
    $('#registerButtonToggle').click(function() {
        $('#container').addClass('active');
    });
    $('#loginButtonToggle').click(function() {
        $('#container').removeClass('active');
    });

    // para cambiar en pantalla de iniciar sesion a crear cuenta en el mismo contendor(Telefono)
    $('#buttonRegister').click(function() {
        $('.sign-in').css({
          opacity: 0,
          zIndex: 0,
        },600);
        $('.sign-up').css({
          opacity: 1,
          zIndex: 1,
        },600);
    });

    $('#buttonCreate').click(function() {
        $('.sign-up').css({
          opacity: 0,
          zIndex: 0,
        },600);
        $('.sign-in').css({
          opacity: 1,
          zIndex: 1,
        },600);
    });
});