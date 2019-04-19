const recipeApiUrl = "http://localhost:8080"

const get = (baseUrl=recipeApiUrl) => (resource, callback) => {
    fetch(baseUrl + resource, {
        method: "GET",
        headers: {
            Accept: "application/json",
        }
    }).then(response => {
        response.json().then(data => {
            callback(data)
        });
    });
}

const save = (baseUrl=recipeApiUrl) => (resource, method, body, callback) => {
    fetch(baseUrl + resource, {
        method: method,
        body: body,
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        }
    }).then(response => {
        response.json().then(data => {
            callback(data)
        });
    });
}

const del = (baseUrl=recipeApiUrl) => (resource, callback) => {
   fetch(baseUrl + resource, {
       method: "DELETE",
       headers: {
           Accept: "application/json",
       }
   }).then(response => {
       callback()
   });
}

export {
    get,
    save,
    del
}