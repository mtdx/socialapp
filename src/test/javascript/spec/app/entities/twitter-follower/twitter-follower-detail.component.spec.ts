import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { SocialappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TwitterFollowerDetailComponent } from '../../../../../../main/webapp/app/entities/twitter-follower/twitter-follower-detail.component';
import { TwitterFollowerService } from '../../../../../../main/webapp/app/entities/twitter-follower/twitter-follower.service';
import { TwitterFollower } from '../../../../../../main/webapp/app/entities/twitter-follower/twitter-follower.model';

describe('Component Tests', () => {

    describe('TwitterFollower Management Detail Component', () => {
        let comp: TwitterFollowerDetailComponent;
        let fixture: ComponentFixture<TwitterFollowerDetailComponent>;
        let service: TwitterFollowerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialappTestModule],
                declarations: [TwitterFollowerDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TwitterFollowerService,
                    EventManager
                ]
            }).overrideTemplate(TwitterFollowerDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TwitterFollowerDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TwitterFollowerService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TwitterFollower(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.twitterFollower).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
