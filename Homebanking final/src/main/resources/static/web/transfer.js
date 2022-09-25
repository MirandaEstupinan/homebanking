const app = Vue.createApp({
    data() {
        return {
            accountOrigin: '',
            accountDestiny: '',
            description: '',
            amount: '',
            clients:[],
            accounts: [],
            own: "own",
            thirds:'thirds',
            accountOrigin2:'',
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
    },
    methods: {
    newTransfer(){
        axios.post('/api/clients/current/transactions', `amount=${this.amount}&description=${this.description}&accountOrigin=${this.accountOrigin}&accountDestiny=${this.accountDestiny}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})            
            .then(response => swal({
                title: "Transfer made successfully" ,
                icon: "success",
                button: "Accept",
            }))
            .then(response => window.location.reload())
            .catch(error => swal({
                icon: 'error',
                title: 'Oops...',
                text: error.response.data,
    }))
                
    },
    logOut(){
        axios.post('/api/logout').
        then(response => location.href = '/web/index.html')
    },
    moneyFormat(balance){ 
        balance = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(balance);
        return balance
    },
    }

}).mount('#app')