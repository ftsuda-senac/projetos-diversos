// Executa fn após carregar DOM
// Equivale ao document.ready() do jQuery.
function ready(fn) {
    if (document.readyState !== 'loading') {
        fn()
    } else {
        document.addEventListener('DOMContentLoaded', fn)
    }
}