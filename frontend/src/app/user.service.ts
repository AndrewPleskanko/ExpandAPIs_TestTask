import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://javapetproject-env.eba-393pcj8c.eu-north-1.elasticbeanstalk.com/users';

  constructor(private http: HttpClient) {
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, user);
  }

  login(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/authenticate`, user);
  }

  getList() {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get(`${this.apiUrl}/all`, { headers });
  }
}

