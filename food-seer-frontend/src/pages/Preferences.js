import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import BudgetQuestion from '../components/BudgetQuestion';
import DietaryRestrictions from '../components/DietaryRestrictions';
import { updateUserPreferences, getCurrentUser } from '../services/api';

function Preferences() {
  const [currentStep, setCurrentStep] = useState(1);
  const [quizData, setQuizData] = useState({
    budget: '',
    dietaryRestrictions: []
  });
  const [saving, setSaving] = useState(false);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Load existing preferences when component mounts
  useEffect(() => {
    const loadPreferences = async () => {
      try {
        const user = await getCurrentUser();
        
        if (user.costPreference || user.dietaryRestrictions) {
          // Parse existing preferences
          const existingBudget = user.costPreference || '';
          const existingDietary = user.dietaryRestrictions || '';
          
          // Parse dietary restrictions
          const dietaryArray = existingDietary ? existingDietary.split(',').map(d => d.trim().toLowerCase()) : [];
          
          setQuizData({
            budget: existingBudget,
            dietaryRestrictions: dietaryArray
          });
        }
      } catch (error) {
        console.error('Error loading preferences:', error);
        // If error, just show empty form
      } finally {
        setLoading(false);
      }
    };

    loadPreferences();
  }, []);

  const handleNext = () => {
    if (currentStep < 2) {
      setCurrentStep(currentStep + 1);
    }
  };

  const handlePrevious = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  const updateQuizData = (field, value) => {
    setQuizData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleFinish = async () => {
    setSaving(true);
    try {
      // Prepare preferences
      const costPreference = quizData.budget;
      const dietaryRestrictions = quizData.dietaryRestrictions.join(', ');

      // Save preferences to backend
      await updateUserPreferences(costPreference, dietaryRestrictions);

      // Navigate to recommendations
      navigate('/recommendations');
    } catch (error) {
      console.error('Error saving preferences:', error);
      alert('Failed to save preferences. Please try again.');
    } finally {
      setSaving(false);
    }
  };

  const renderCurrentStep = () => {
    switch (currentStep) {
      case 1:
        return (
          <BudgetQuestion
            budget={quizData.budget}
            onUpdate={updateQuizData}
            onNext={handleNext}
            onPrevious={handlePrevious}
            canGoNext={quizData.budget !== ''}
          />
        );
      case 2:
        return (
          <DietaryRestrictions
            restrictions={quizData.dietaryRestrictions}
            onUpdate={updateQuizData}
            onNext={handleFinish}
            onPrevious={handlePrevious}
            canGoNext={!saving}
            isLastStep={true}
          />
        );
      default:
        return null;
    }
  };

  if (loading) {
    return (
      <div className="App">
        <div className="loading">Loading preferences...</div>
      </div>
    );
  }

  return (
    <div className="App">
      {renderCurrentStep()}
    </div>
  );
}

export default Preferences;

