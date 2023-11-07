function getJson(url) {
  const promise = new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.onload = function () {
      if (xhr.status >= 200 && xhr.status < 400) {
        // SUCESSO
        resolve(JSON.parse(xhr.responseText));
      } else {
        // ERROS
        const objErro = {
          codigo: xhr.status,
          mensagem: 'Erro'
        };
        reject(objErro);
      }
    }
    xhr.send();
  });
  return promise;
}

function postJson(url, dados) {
  const promise = new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader('content-type', 'application/json')
    xhr.onload = function () {
      if (xhr.status >= 200 && xhr.status < 400) {
        // SUCESSO
        if (xhr.responseText) {
          resolve(JSON.parse(xhr.responseText));
        } else {
          resolve();
        }
      } else {
        // ERROS
        if (xhr.status === 400) {
          const errorResponse = JSON.parse(xhr.responseText);
          const mensagemErros = []
          if (errorResponse.errors?.length > 0) {
            for (const error of errorResponse.errors) {
              mensagemErros.push({
                field: error.field,
                mensagem: error.defaultMessage
              });
            }
          }
          reject(mensagemErros);
        } else {
          const objErro = {
            codigo: xhr.status,
            mensagem: 'Erro'
          };
          reject(objErro);
        }
      }
    };
    xhr.send(JSON.stringify(dados));
  });
  return promise;
}