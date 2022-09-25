const params = new URLSearchParams(window.location.search);
const id = params.get('id');
console.log(id);

const app = Vue.createApp({
    data() {
        return {
            clients:[],
            accounts:[],
            accountId:[],
            transactions:[],
            balance:'',
            accountBalance:[],
            toDate:'',
            fromDate:'',
            clientAccount:'',
            email:'',
        }
    },

    created() {
        axios.get('/api/clients/current')
        .then((response)=>{
            this.clients = response.data;
            this.accounts = this.clients.accounts;
            console.log(this.accounts);      
            this.accountId = this.accounts.find(account => account.id == id);
            this.clientAccount = this.accountId.number; 
            this.email = this.clients.email;      
            this.transactions = this.accountId.transactions.sort((b,a) => {
                return a.id - b.id
            });
            console.log(this.clientAccount)
            
        })
        .catch(function (error) {
            console.log(error);
        })
    },
    methods:{
        formatDate(date){            
            newDate = new Date(date).toLocaleString();
            return newDate
        },
        moneyFormat(balance){ 
            balance = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(balance);
            return balance
        },
        logOut(){
            axios.post('/api/logout').
            then(response => location.href = '/web/index.html')
        },
        filter(){
            axios.post('/api/transactions/filtered',{
                "fromDate": this.fromDate,
                "toDate": this.toDate,
                "clientAccount": this.clientAccount,
            })
            .then(                
                response => swal({
                title: "Download done successfully" ,
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
        
        
    }

}).mount('#app')