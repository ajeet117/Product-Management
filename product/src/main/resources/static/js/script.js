let brand1=document.querySelector(".drop-brand");
let brandmenu1=document.querySelector(".brand-option");
brand1.onclick=function()
{
   brandmenu1.classList.toggle("d-none");
}

let product1=document.querySelector(".drop-product");
let productmenu1=document.querySelector(".product-option")
product1.onclick=function()
{
    productmenu1.classList.toggle("d-none");
}

let profile1=document.querySelector(".drop-profile");
let profilemenu1=document.querySelector(".profile-menu");
profile1.onclick=function()
{
    profilemenu1.classList.toggle("d-none");
}

let keyword=document.querySelector("#keyword");
let clearBtn=document.querySelector('btnClear');
clearBtn.onclick=function()
{
	keyword.innerHTML="";
}