const app = Vue.createApp({
    data() {
        return {
            accounts:[],
            clients:[],
            loans:[],
            spinner: false,
            accountsFilter:[],
            number:'',
            saving:'',
            current:'',
            accountType:'',
            email:'',
        }
    },

    created() {
        this.create()
        
    },
    methods:{
        moneyFormat(balance){ 
            balance = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(balance);
            return balance
        },
        logOut(){
            axios.post('/api/logout').
            then(response => window.location.href = '/web/index.html')
        },
        newAccount(){
            axios.post('/api/clients/current/accounts', `accountType=${this.accountType}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => swal({
                title: "Account Created successfully" ,
                icon: "success",
                button: "Accept",
            }))
            .then(response => {                
                this.create()                
            })
            .then(response => window.location.reload())
            .catch(response => swal({
                title: "Cannot create more than 3 accounts",
                icon: "error",
                button: "Accept",
            }))
        },
        create(){
            axios.get('/api/clients/current')
            .then((response) =>{
            this.clients = response.data;   
            this.email = this.clients.email;         
            this.accounts = this.clients.accounts.sort((a,b) => {
                return a.id - b.id
            });
            this.accountsFilter = this.accounts.filter(account => account.accountActive == true)
            this.loans = this.clients.loans;  
            
        })
        .catch(function (error) {
            console.log(error);
        });
        },
        deleteAccount(account){
            this.number = account.number
            axios.patch('/api/clients/current/accounts/delete' , `number=${this.number}`)
            .then(                
                response => swal({
                title: "Account deleted successfully" ,
                icon: "success",
                button: "Accept",
            }))
            .then(response => window.location.reload())
            .catch(error =>
                swal({
                    icon: 'error',
                    title: 'Oops...',
                    text: error.response.data,
        }))
        }
    },
    

}).mount('#app')



