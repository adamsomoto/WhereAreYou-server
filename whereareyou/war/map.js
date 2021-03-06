var map;
var il = {lat: 32, lng: 34};
var friendMarkers = [];
var myMarker;

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
      zoom: 4,
      center: il
    });  
}

var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http) {
    
    var myTicket = getParameterByName('myTicket');
    var friendTicket = getParameterByName('friendTicket');
 
    if(friendTicket){
        doGetAndMarkFriend($http, friendTicket);
    }   
    
    if (navigator.geolocation) {
        markMyLocation();
        if(myTicket){
            doPostWithMyLocation($http, myTicket);
        }
    }   
   
});

function doGetAndMarkFriend($http, friendTicket){
    $http.get("http://2-dot-whereareyou-148008.appspot.com/Users?umid="+friendTicket)
    .then(function(response) { 
        for (i in friendMarkers) {
          friendMarkers[i].setMap(null);
        }
        friendMarkers = [];
        for(i in response.data){
            var iter = response.data[i];
            var marker = new google.maps.Marker({
                position: {lat: iter.lat, lng: iter.lng},
                map: map
            });
            friendMarkers.push(marker);
        }       
    });
}

function markMyLocation($http, myTicket){
    navigator.geolocation.getCurrentPosition(function(position){
        var coords = position.coords;
        var latlng = {lat: coords.latitude, lng: coords.longitude};
        myMarker = new google.maps.Marker({
            position: latlng,
            map: map,
            icon: 'beachflag.png'
        });
    });
}

function doPostWithMyLocation($http, myTicket){
    navigator.geolocation.getCurrentPosition(function(position){
        var coords = position.coords;   
        $http.post("http://2-dot-whereareyou-148008.appspot.com/Users?umid="+myTicket+'&lat='+coords.latitude+'&lng='+coords.longitude);
    });
}

function getParameterByName(name) {
    var url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}