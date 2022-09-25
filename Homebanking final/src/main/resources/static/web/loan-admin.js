const app = Vue.createApp({
    data() {
        return {
            name: '',
            maxAmount: '',
            payments: [],
            interest: '',
            clients:'',
            email:'',
        }
    },

    created() {
        axios.get('/api/clients/current')
        .then((response) =>{
        this.clients = response.data;
        this.accounts = this.clients.accounts;
        this.email = this.clients.email;      
        })
        .catch(function (error) {
            console.log(error);
        });

    },
    methods: {
        newLoanAdmin() {
            axios.post("/api/loans/admin", `name=${this.name}&payments=${this.payments}&maxAmount=${this.maxAmount}&interest=${this.interest}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => swal({
                    title: "Successful " ,
                    icon: "success",
                    button: "Accept",
                }))
                .catch(error =>
                    swal({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'error',
                    }))
        },
        moneyFormat(balance) {
            balance = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(balance);
            return balance
        },
        logOut() {
            axios.post('/api/logout').
                then(response => window.location.href = '/web/index.html')
        },
    },


}).mount('#app')
