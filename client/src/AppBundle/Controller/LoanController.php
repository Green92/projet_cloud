<?php

namespace AppBundle\Controller;

use AppBundle\Entity\LoanRequest;
use AppBundle\Entity\LoanResponse;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\HttpFoundation\Request;
use GuzzleHttp;

/**
 * @Route("/loan")
 */
class LoanController extends Controller
{
    private $client;

    public function __construct()
    {
        $this->client = new GuzzleHttp\Client([
            'base_uri' => 'http://loanapproval.herokuapp.com/loan/'
        ]);
    }

    private function getAddForm($approval)
    {
        return $this->createFormBuilder($approval)
            ->add('amount', NumberType::class, array('required' => true))
            ->getForm();
    }

    /**
     * @Route("/{id}/requestLoanApproval", name="request_loan")
     */
    public function editAction($id, Request $request)
    {
//        $response = $this->client->request('GET', $id);

        $loan = new LoanRequest();
        $loan->setAccountId($id);

        $form = $this->getAddForm($loan);

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $response = $this->client->request('POST', null, [
                'json' => $loan->getApiArray()
            ]);

            if ($response->getStatusCode() == 202) {
                $loanResponse = new LoanResponse();
                $loanResponse->loadApiJson($response->getBody());

                $this->get('session')->getFlashBag()->add('success',
                    'Loan Approval request has been sent.'
                );
                $this->get('session')->getFlashBag()->add('notice',
                    sprintf('Your loan request is %s', $loanResponse->getResponse())
                );
            }
            else {
                $this->get('session')->getFlashBag()->add('error',
                    'An error occurred during loan approval request.'
                );
            }

            return $this->redirectToRoute('get_account');
        }

        return $this->render('loan/request.html.twig', array(
            'form' => $form->createView()
        ));
    }
}
