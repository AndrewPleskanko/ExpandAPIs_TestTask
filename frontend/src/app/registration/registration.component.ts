import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../user.service";
import {FormControl, FormGroup, Validators, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent implements OnInit {
  errorMessage = '';
  registrationForm!: FormGroup;

  constructor(private userService: UserService, private router: Router) {
    this.registrationForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),

    });
  }

  ngOnInit() {
    this.registrationForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      email: new FormControl('', Validators.required)
    });
  }

  register() {
    const {username, password, email} = this.registrationForm.value;
    this.userService.register({username, password, email}).subscribe(
      data => {
        this.router.navigate(['/login']);
      },
      error => {
        this.errorMessage = 'Registration failed';
      }
    );
  }
}
