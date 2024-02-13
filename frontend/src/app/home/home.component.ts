import {Component} from '@angular/core';
import {UserService} from "../user.service";
import {User} from "../user";
import {map} from "rxjs";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  errorMessage: string;
  userList: User[];
  userListHtml = ''; // This will hold the HTML string

  constructor(private userService: UserService) {
    this.errorMessage = '';
    this.userList = []; // initialize userList as an empty array
  }

  getList() {
    this.userService.getList().pipe(
      map(response => response as User[])
    ).subscribe(
      (userList: User[]) => {
        this.userList = userList;
        this.userListHtml = userList.map(user => `<li>User: ${user.username}</li>`).join('');
      },
      (error: any) => {
        console.log(error);
        this.errorMessage = 'Only admin has access to this functionality.';
      }
    );
  }
}
