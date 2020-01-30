import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from '@ikubinfo/core/models/post';
import { PostService } from '@ikubinfo/core/services/post.service';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';

@Component({
  selector: 'ikubinfo-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  postForm: FormGroup;
  post: Post;

  constructor(private fb: FormBuilder, private active: ActivatedRoute, private postService: PostService, private logger: LoggerService, private router: Router) { }

  ngOnInit() {
    this.initializeForm();
    this.loadData();
  }

  loadData(): void {
    const id = this.active.snapshot.paramMap.get('id');
    if (id) {
      this.postService.readAsync(Number(id)).toPromise()
        .then(data => {
          this.post = data;
          this.postForm.get('title').setValue(this.post.title);
          this.postForm.get('description').setValue(this.post.body);
        }).catch(_ => {
          this.logger.error('Error', 'An error accured');
        });
    }
  }

  reset(): void {
    this.fillForm(this.post);
  }

  initializeForm(): void {
    this.postForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

  fillForm(data: Post = {}): void {
    this.postForm.get('title').setValue(data.title);
    this.postForm.get('description').setValue(data.body);
  }

  getData(): Post {
    return {
      title: this.postForm.get('title').value,
      body: this.postForm.get('description').value
    }

  }

  submit(): void {
    if (this.post) {
      this.postService.editAsync(this.post.id, this.getData()).toPromise().then(_ => {
        this.logger.info('Success', 'Added !');
        this.router.navigate(['posts'], { relativeTo: this.active.parent });

      }).catch(_ => {
        this.logger.error('Error', 'An error accured');
      });
    }
    else {
      this.postService.createAsync(this.getData()).toPromise().then(_ => {
        this.logger.info('Success', 'Created ');
        this.router.navigate(['posts'], { relativeTo: this.active.parent });
      }).catch(_ => {
        this.logger.error('Error', 'An error accured');
      });

    }

  }

}
