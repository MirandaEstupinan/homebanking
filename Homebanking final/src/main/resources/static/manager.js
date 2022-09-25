
const app = Vue.createApp({
    data() {
        return {
            clients: [],
            firstName: '',
            lastName: '',
            email: '',
            id:'',
        }
    },
    created() {
        this.cargarDatos()

    },
    methods:{
        cargarDatos(){
            axios.get('/rest/clients')
            .then((response) => {
                this.clients = response.data._embedded.clients;
                console.log(this.clients)
            })
            .catch(function (error) {
                console.log(error);
            });
        },
        addClient(){
            axios.post('/rest/clients',{
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email,
            }).then(this.cargarDatos())
            
            .catch(function (error) {
                console.log(error);
            })
        },
    }

}).mount('#app')

