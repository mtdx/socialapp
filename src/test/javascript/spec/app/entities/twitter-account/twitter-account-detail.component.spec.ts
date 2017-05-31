import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { SocialappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TwitterAccountDetailComponent } from '../../../../../../main/webapp/app/entities/twitter-account/twitter-account-detail.component';
import { TwitterAccountService } from '../../../../../../main/webapp/app/entities/twitter-account/twitter-account.service';
import { TwitterAccount } from '../../../../../../main/webapp/app/entities/twitter-account/twitter-account.model';

describe('Component Tests', () => {

    describe('TwitterAccount Management Detail Component', () => {
        let comp: TwitterAccountDetailComponent;
        let fixture: ComponentFixture<TwitterAccountDetailComponent>;
        let service: TwitterAccountService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialappTestModule],
                declarations: [TwitterAccountDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TwitterAccountService,
                    EventManager
                ]
            }).overrideTemplate(TwitterAccountDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TwitterAccountDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TwitterAccountService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TwitterAccount(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.twitterAccount).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
