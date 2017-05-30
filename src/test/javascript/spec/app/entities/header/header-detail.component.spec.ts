import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { SocialappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { HeaderDetailComponent } from '../../../../../../main/webapp/app/entities/header/header-detail.component';
import { HeaderService } from '../../../../../../main/webapp/app/entities/header/header.service';
import { Header } from '../../../../../../main/webapp/app/entities/header/header.model';

describe('Component Tests', () => {

    describe('Header Management Detail Component', () => {
        let comp: HeaderDetailComponent;
        let fixture: ComponentFixture<HeaderDetailComponent>;
        let service: HeaderService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialappTestModule],
                declarations: [HeaderDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    HeaderService,
                    EventManager
                ]
            }).overrideTemplate(HeaderDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(HeaderDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HeaderService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Header(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.header).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
