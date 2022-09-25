const app = Vue.createApp({
    data() {
        return {
            loans:[],
            id:"",
            amount:"",
            payments:[],
            accountDestination:"",
            clients:[],
            loanName:'',
            payment:"",
            accounts:[],
            interest: [],
            mortgage:'',
            personal:'',
            car:'',
            loanId:'',
            newLoan1:[],
            email:'',
        }
    },

    created() {
        axios.get('/api/loans')
        .then((response) =>{
            this.loans = response.data;            
        }),
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
    methods:{

        newLoan() {
            axios.post("/api/clients/current/loans", {
                "id":`${this.loanId}`,
                "amount":`${this.amount}`,
                "payments":`${this.payments}`,
                "accountDestination":`${this.accountDestination}`,
            })
                .then(response => swal({
                    title: "Successful request" ,
                    icon: "success",
                    button: "Accept",
                }))
                .then(response => location.href = '/web/accounts.html')
                .catch(error => swal({
                    icon: 'error',
                    title: 'Oops...',
                    text: error.response.data,
        }))
        },
        moneyFormat(balance){ 
            balance = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(balance);
            return balance
        },
        logOut(){
            axios.post('/api/logout').
            then(response => window.location.href = '/web/index.html')
        },
    },
    

}).mount('#app')
