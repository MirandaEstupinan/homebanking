const app = Vue.createApp({
    data() {
        return {
            email:'',
            password:'',
            firstName:'',
            lastName:'',
            emailRegister:'',
            passwordRegister:'',

        }
    },

    created() {
        
    },
    methods:{
        login(){
            axios.post('/api/login',`email=${this.email}&password=${this.password}`,
                {headers:{'content-type':'application/x-www-form-urlencoded'}
            })
            .then(response => location.href = '/web/accounts.html')
            .catch(response =>
                swal({
                icon: 'error',
                title: 'Oops...',
                text: 'User not found',
            }))
            },
        register(){
            axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => 
                swal({
                title: "Registered Successfully",
                icon: "success",
                button: "Accept",
            }))
            .then(response => axios.post('/api/login',`email=${this.emailRegister}&password=${this.passwordRegister}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}
            }))
            .then(response => location.href = '/web/accounts.html')
            .catch(error =>
                swal({
                icon: 'error',
                title: 'Oops...',
                text: error.response.data,
            }))
        },        
    }

}).mount('#app')

